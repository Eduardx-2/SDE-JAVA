# Normalización de la base de datos — Sistema de Envíos

**Base de datos:** `paquetes_system` · MariaDB · InnoDB · `utf8mb4_uca1400_ai_ci`

**Tablas actuales:** `usuarios`, `paquetes`, `envios`, `destinatarios`, `historial_envios`, `paquetes_pendientes`, `roles`

Este documento explica, para alguien que no conoce el proyecto, qué está bien, qué sobra y cómo dejar el esquema en tercera forma normal (3NF) sin romper el código que ya usa la base.

---

## 1. Resumen

El esquema cumple la primera forma normal (columnas atómicas) y tiene claves primarias y foráneas. Los problemas reales son:

1. Datos del destinatario repetidos en dos sitios.
2. Estados y roles guardados como texto libre.
3. Una cola de paquetes sin destinatario que debe limpiarse cuando el destinatario se agrega.

Abajo: cada hallazgo, por qué importa y la corrección propuesta. Los cambios que alteran tablas ya mapeadas por la aplicación se hacen **por fases** y con aprobación antes de cada fase.

---

## 2. Hallazgos

### 2.1 `envios` duplica el destinatario (viola 3NF)

`envios` tiene `destinatario` y `direccion` en texto. Los mismos datos viven en `destinatarios` (`nombre`, `direccion`), con relación 1:1 por `id_envio`.

**Problema:** si se actualiza un lado y no el otro, quedan valores contradictorios.

**Corrección:** dejar nombre y dirección solo en `destinatarios`; quitar esas columnas de `envios` cuando la aplicación deje de leerlas.

### 2.2 Estados como texto libre

`envios.estado` y `historial_envios.estado_anterior` / `estado_nuevo` son cadenas libres. Se cuelan variantes (`REGISTRADO`, `Registrado`, etc.).

**Corrección:** catálogo `estados_envio` (`id_estado`, `codigo`, `descripcion`, `es_final`) y FKs en lugar del texto.

**Estados y transiciones acordados:**

- `registrado` → `en_bodega` → `en_transito` → `en_reparto` → `entregado`
- `en_reparto` → `fallido` → `en_reparto` (reintento) o `devuelto`
- `cancelado` solo desde `registrado` o `en_bodega`
- Finales: `entregado`, `devuelto`, `cancelado`

Las transiciones válidas van en `transiciones_estado` (origen, destino). El historial solo debe aceptar saltos permitidos; el primer evento tiene `estado_anterior` NULL. MariaDB no valida ese par con un `CHECK`: se valida con un trigger o en la capa de la aplicación (pendiente de decidir).

### 2.3 Roles como texto libre

`usuarios.rol` es texto. Hoy solo existe `admin`.

**Corrección:** catálogo `roles` (ya creado). Roles previstos para más adelante: `operador` y `cliente`. Vincular `usuarios` a `roles` es un `ALTER` sobre una tabla que la aplicación mapea: va en una migración aparte, con aprobación.

### 2.4 Usuario del envío frente al dueño del paquete

`paquetes.id_usuario` y `envios.id_usuario` apuntan ambos a `usuarios`.

**Acordado:** se mantienen. Quien registra el envío puede ser distinto del dueño del paquete. En documentación y migraciones futuras, el de `envios` se llama `id_usuario_registro` para que quede claro.

### 2.5 Destinatario 1:1 (foto del momento)

**Acordado:** se queda en 1:1. El destinatario de un envío es una foto: si se compartiera entre envíos y alguien cambiara la dirección, cambiarían también los envíos viejos. Si más adelante hace falta reutilizar contactos, se agrega una libreta `contactos` y al crear el envío se **copian** los datos a `destinatarios`.

### 2.6 Paquetes sin destinatario

Cuando al crear un paquete se elige no agregar destinatario aún, el paquete entra a `paquetes_pendientes` con estado `SIN_DESTINATARIO`. El código del paquete no se copia: se obtiene con un `JOIN` a `paquetes`.

**Regla:** al agregar el destinatario, **se borra** la fila de la cola. Si no, la cola miente.

### 2.7 Contraseñas (fuera de formas normales)

`usuarios.password` debe guardar un hash (bcrypt/argon2), nunca texto plano. Conviene corregirlo en la misma línea de migraciones de seguridad.

---

## 3. Modelo objetivo (3NF)

```text
roles(id_rol PK, nombre UNIQUE, descripcion)

usuarios(id_usuario PK, nombre, apellido, correo UNIQUE,
         telefono UNIQUE, password_hash, id_rol FK->roles)

estados_envio(id_estado PK, codigo UNIQUE, descripcion, es_final)

transiciones_estado(id_estado_origen FK, id_estado_destino FK,
                    PK(origen, destino))

paquetes(id_paquete PK, codigo UNIQUE, descripcion, peso, largo, ancho,
         alto, valor, id_usuario FK->usuarios)

paquetes_pendientes(id_paquete PK FK->paquetes, estado, fecha_ingreso)
   -- se borra cuando el paquete recibe destinatario

destinatarios(id_destinatario PK, id_envio FK->envios UNIQUE, nombre,
              telefono, correo, direccion, referencias, fecha_registro)

envios(id_envio PK, codigo_seguimiento UNIQUE, id_paquete FK->paquetes,
       id_usuario_registro FK->usuarios, id_estado FK->estados_envio,
       fecha_envio, fecha_entrega)
   -- sin columnas de destinatario/dirección/estado en texto

historial_envios(id_historial PK, id_envio FK->envios,
                 id_estado_anterior FK NULL, id_estado_nuevo FK,
                 fecha_cambio, observaciones, id_usuario FK NULL)
```

---

## 4. Migración por fases

Las tablas `envios` y `usuarios` las usa la aplicación. No se borran columnas mientras el código las lea.

**Fase 1 — solo agregar (ya en curso)**  
Crear catálogos y tablas nuevas (`roles`, `paquetes_pendientes`; luego `estados_envio` / `transiciones_estado`). No se toca Java.

**Fase 2 — cambiar la aplicación**  
Actualizar entidades para usar FKs y la cola de pendientes; dejar de leer columnas de texto redundantes.

**Fase 3 — limpiar**  
Poner `NOT NULL` y FKs definitivas; borrar columnas redundantes; migrar contraseñas a hash.

Cada fase necesita aprobación antes de ejecutarse.

---

## 5. Estado de decisiones

| Tema | Decisión |
| --- | --- |
| Usuario del envío | Mantener como registro (`id_usuario_registro`) |
| Destinatario | 1:1 (foto del momento) |
| Roles | Catálogo `roles` con `admin`; vincular `usuarios` después |
| Estados | Catálogo + transiciones; validación en trigger o aplicación |
| Cola sin destinatario | `paquetes_pendientes`; borrar fila al completar destinatario |
| Descomponer dirección | Opcional; no prioritario |
