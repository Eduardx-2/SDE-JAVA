# Tablas de la base de datos — Sistema de Envíos

Base de datos: `paquetes_system` (MariaDB / InnoDB, `utf8mb4` / `utf8mb4_uca1400_ai_ci`).

Este documento describe **solo el esquema**: qué hace cada tabla y el detalle de sus columnas. No incluye rutas de proyecto ni credenciales.

```
usuarios 1──N paquetes 1──0..1 paquetes_pendientes
    │              │
    │              └──N envios 1──1 destinatarios
    │                     │
    │                     └──N historial_envios
    │                            └── (opcional) usuarios
roles   (catálogo; aún no enlazado a usuarios)
```

---

## 1. `usuarios`

### Para qué sirve

Cuentas del sistema: quien inicia sesión, registra paquetes o cambia estados de envío.

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_usuario` | `INT(11) AUTO_INCREMENT` | NO | PK. Identificador del usuario. |
| `nombre` | `VARCHAR(60)` | NO | Nombre. |
| `apellido` | `VARCHAR(60)` | NO | Apellido. |
| `correo` | `VARCHAR(70)` UNIQUE | NO | Correo de acceso; único. |
| `telefono` | `VARCHAR(20)` UNIQUE | NO | Teléfono; único. |
| `password` | `TEXT` | NO | Credencial de acceso (debe almacenarse como hash). |
| `rol` | `VARCHAR(10)` | NO | Rol en texto (`admin` hoy). El catálogo `roles` ya existe; el enlace por FK es una migración futura. |

---

## 2. `roles`

### Para qué sirve

Catálogo de roles del sistema. Hoy solo tiene `admin`. Evita seguir inventando valores libres en texto. **No** altera `usuarios` todavía: vincular `usuarios.rol` a esta tabla requiere un `ALTER` aprobado aparte.

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_rol` | `INT(11) AUTO_INCREMENT` | NO | PK. |
| `nombre` | `VARCHAR(20)` UNIQUE | NO | Código del rol (`admin`; previstos: `operador`, `cliente`). |
| `descripcion` | `VARCHAR(100)` | SÍ | Texto breve de qué puede hacer ese rol. |

### Datos iniciales

| id_rol | nombre | descripcion |
| --- | --- | --- |
| 1 | `admin` | Acceso completo al sistema |

---

## 3. `paquetes`

### Para qué sirve

Cada paquete físico registrado: medidas, peso, valor y dueño.

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_paquete` | `INT(11) AUTO_INCREMENT` | NO | PK. |
| `codigo` | `VARCHAR(30)` UNIQUE | NO | Código interno del paquete. |
| `descripcion` | `VARCHAR(255)` | SÍ | Descripción libre. |
| `peso` | `DECIMAL(8,3)` | NO | Peso. |
| `largo` | `DECIMAL(8,2)` | NO | Largo. |
| `ancho` | `DECIMAL(8,2)` | NO | Ancho. |
| `alto` | `DECIMAL(8,2)` | NO | Alto. |
| `valor` | `DECIMAL(10,2)` | NO | Valor declarado. |
| `id_usuario` | `INT(11)` | NO | FK a `usuarios`. Dueño / quien registró el paquete. |

### Claves foráneas

- `id_usuario` → `usuarios(id_usuario)`

---

## 4. `paquetes_pendientes`

### Para qué sirve

Cola de paquetes que **aún no tienen destinatario**. Cuando al crear un paquete se elige no agregar destinatario (`N`), el `id_paquete` entra aquí con estado `SIN_DESTINATARIO`. Es como un producto en fila que todavía no tiene “nombre” de entrega.

El `codigo` del paquete **no se duplica**: se obtiene con un `JOIN` a `paquetes`.

**Regla de limpieza:** cuando se agrega el destinatario, **se borra** la fila de esta tabla. Si no, la cola miente.

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_paquete` | `INT(11)` | NO | PK y FK a `paquetes.id_paquete`. Un paquete aparece una sola vez en la cola. |
| `estado` | `VARCHAR(30)` DEFAULT `'SIN_DESTINATARIO'` | NO | Motivo de la espera. Hoy: sin destinatario. |
| `fecha_ingreso` | `DATETIME` DEFAULT `current_timestamp()` | NO | Cuándo entró a la cola. |

### Claves foráneas

- `id_paquete` → `paquetes(id_paquete)` con **`ON DELETE CASCADE`**: si se borra el paquete, sale de la cola.

### SELECT de ejemplo (id, código y estado)

```sql
SELECT pp.id_paquete, p.codigo, pp.estado, pp.fecha_ingreso
FROM paquetes_pendientes pp
INNER JOIN paquetes p ON p.id_paquete = pp.id_paquete
WHERE pp.estado = 'SIN_DESTINATARIO'
ORDER BY pp.fecha_ingreso;
```

---

## 5. `envios`

### Para qué sirve

Flujo operativo del envío: seguimiento, estado actual, fechas y vínculo al paquete y al usuario que lo registró. Todavía guarda `destinatario` y `direccion` en texto (redundantes con `destinatarios`); quitarlas es una migración por fases.

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_envio` | `INT(11) AUTO_INCREMENT` | NO | PK. |
| `codigo_seguimiento` | `VARCHAR(30)` UNIQUE | NO | Código de tracking. |
| `id_paquete` | `INT(11)` | NO | FK a `paquetes`. |
| `id_usuario` | `INT(11)` | NO | FK a `usuarios`. Quien registró el envío (no necesariamente el dueño del paquete). |
| `destinatario` | `VARCHAR(100)` | NO | Nombre en texto (redundante con `destinatarios`; migración futura). |
| `direccion` | `VARCHAR(255)` | NO | Dirección en texto (redundante; migración futura). |
| `estado` | `VARCHAR(30)` DEFAULT `'REGISTRADO'` | NO | Estado actual en texto (catálogo futuro). |
| `fecha_envio` | `DATETIME` DEFAULT `current_timestamp()` | NO | Alta del envío. |
| `fecha_entrega` | `DATETIME` | SÍ | Fecha de entrega, si aplica. |

### Claves foráneas

- `id_usuario` → `usuarios(id_usuario)`
- `id_paquete` → `paquetes(id_paquete)`

---

## 6. `destinatarios`

### Para qué sirve

Quién recibe el paquete y sus datos de contacto, en relación **1:1** con cada envío (foto del momento).

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_destinatario` | `INT(11) AUTO_INCREMENT` | NO | PK. |
| `id_envio` | `INT(11)` UNIQUE | NO | FK a `envios`. Un solo destinatario por envío. |
| `nombre` | `VARCHAR(100)` | NO | Nombre de quien recibe. |
| `telefono` | `VARCHAR(20)` | NO | Teléfono de contacto. |
| `correo` | `VARCHAR(70)` | SÍ | Correo opcional. |
| `direccion` | `VARCHAR(255)` | SÍ | Dirección de entrega. |
| `referencias` | `VARCHAR(255)` | SÍ | Notas de ubicación. |
| `fecha_registro` | `DATETIME` DEFAULT `current_timestamp()` | NO | Cuándo se cargó. |

### Claves foráneas

- `id_envio` → `envios(id_envio)` con **`ON DELETE CASCADE`**.

---

## 7. `historial_envios`

### Para qué sirve

Cada cambio de estado de un envío: de dónde venía, a dónde pasó, cuándo, con qué nota y (si aplica) qué usuario lo hizo. Relación **1 envío → N eventos**.

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_historial` | `INT(11) AUTO_INCREMENT` | NO | PK. |
| `id_envio` | `INT(11)` | NO | FK a `envios`. |
| `estado_anterior` | `VARCHAR(30)` | SÍ | Estado previo. NULL en el primer evento. |
| `estado_nuevo` | `VARCHAR(30)` | NO | Estado al que pasó. |
| `fecha_cambio` | `DATETIME` DEFAULT `current_timestamp()` | NO | Momento del cambio. |
| `observaciones` | `VARCHAR(255)` | SÍ | Notas del cambio. |
| `id_usuario` | `INT(11)` | SÍ | Quién hizo el cambio. NULL si fue el sistema o el usuario se eliminó. |

### Claves foráneas

- `id_envio` → `envios(id_envio)` **`ON DELETE CASCADE`**
- `id_usuario` → `usuarios(id_usuario)` **`ON DELETE SET NULL`**

### SELECT de ejemplo

```sql
SELECT h.fecha_cambio, h.estado_anterior, h.estado_nuevo, h.observaciones, u.nombre
FROM historial_envios h
LEFT JOIN usuarios u ON u.id_usuario = h.id_usuario
WHERE h.id_envio = 1
ORDER BY h.fecha_cambio;
```

---
