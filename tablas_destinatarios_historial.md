 # Tablas nuevas: `destinatarios` e `historial_envios`

Base de datos: `paquetes_system` (MariaDB / InnoDB, `utf8mb4` / `utf8mb4_uca1400_ai_ci`).

Estas dos tablas **no modifican** `usuarios`, `paquetes` ni `envios`. Ambas cuelgan de `envios.id_envio`. El código Java / JPA existente no se toca en este cambio; solo se documenta el esquema SQL.

---

## 1. `destinatarios`

### Para qué sirve

Guarda **quién recibe el paquete** y sus datos de contacto, separados del registro de envío. Así `envios` sigue siendo el flujo operativo (seguimiento, estado, fechas) y los datos del receptor viven en una fila propia, con relación **1:1** con cada envío (`id_envio` UNIQUE).

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_destinatario` | `INT(11) AUTO_INCREMENT` | NO | PK. Identificador del destinatario. |
| `id_envio` | `INT(11)` UNIQUE | NO | FK a `envios.id_envio`. Un solo destinatario por envío. |
| `nombre` | `VARCHAR(100)` | NO | Nombre de quien recibe. |
| `telefono` | `VARCHAR(20)` | NO | Teléfono de contacto. |
| `correo` | `VARCHAR(70)` | SÍ | Correo opcional; no siempre se tiene. |
| `direccion` | `VARCHAR(255)` | SÍ\* | Dirección de entrega (detalle del receptor). |
| `referencias` | `VARCHAR(255)` | SÍ | Referencias de ubicación / notas (ej. “casa azul”). |
| `fecha_registro` | `DATETIME` DEFAULT `current_timestamp()` | NO | Cuándo se cargó el destinatario. |

\*Según el DDL acordado: `direccion` y `referencias` pueden ser NULL si el envío aún no tiene dirección fina; `nombre` y `telefono` son obligatorios.

### Claves foráneas

- `id_envio` → `envios(id_envio)` con **`ON DELETE CASCADE`**: si se borra el envío, se borra su destinatario.
- Relación con `envios`: **1 envío ↔ 1 destinatario**.

### SELECT de ejemplo

```sql
SELECT d.id_destinatario, d.nombre, d.telefono, d.correo, d.direccion, d.referencias,
       e.codigo_seguimiento, e.estado
FROM destinatarios d
INNER JOIN envios e ON e.id_envio = d.id_envio
WHERE e.id_envio = 1;
```

---

## 2. `historial_envios`

### Para qué sirve

Registra **cada cambio de estado** de un envío: de qué estado venía, a cuál pasó, cuándo, con qué observación y (si aplica) qué usuario lo hizo. Sirve para auditoría y para mostrar la línea de tiempo del paquete sin pisar el `estado` actual en `envios`.

Relación con `envios`: **1 envío → N filas de historial**.

### Columnas

| Columna | Tipo | NULL | Descripción |
| --- | --- | --- | --- |
| `id_historial` | `INT(11) AUTO_INCREMENT` | NO | PK. |
| `id_envio` | `INT(11)` | NO | FK a `envios.id_envio`. Envío al que pertenece el evento. |
| `estado_anterior` | `VARCHAR(30)` | SÍ | Estado previo. NULL en el **primer** evento (paso a `REGISTRADO`), porque no hay estado anterior. |
| `estado_nuevo` | `VARCHAR(30)` | NO | Estado al que pasó el envío. |
| `fecha_cambio` | `DATETIME` DEFAULT `current_timestamp()` | NO | Momento del cambio. |
| `observaciones` | `VARCHAR(255)` | SÍ | Notas libres del cambio (opcional). |
| `id_usuario` | `INT(11)` | SÍ | FK a `usuarios.id_usuario`. Quién hizo el cambio. NULL si fue el sistema o si el usuario se eliminó (`ON DELETE SET NULL`). |

### Índices y FKs

- Índice compuesto `idx_historial_envio_fecha (id_envio, fecha_cambio)`: consulta típica por envío ordenada en el tiempo; también respalda la FK a `envios`.
- Índice `idx_historial_usuario (id_usuario)`.
- `id_envio` → `envios(id_envio)` **`ON DELETE CASCADE` / `ON UPDATE CASCADE`**.
- `id_usuario` → `usuarios(id_usuario)` **`ON DELETE SET NULL` / `ON UPDATE CASCADE`**.

### SELECT de ejemplo

```sql
SELECT h.fecha_cambio, h.estado_anterior, h.estado_nuevo, h.observaciones, u.nombre
FROM historial_envios h
LEFT JOIN usuarios u ON u.id_usuario = h.id_usuario
WHERE h.id_envio = 1
ORDER BY h.fecha_cambio;
```

---

## Relación general

```
usuarios 1──N envios 1──1 destinatarios
              │
              └──N historial_envios
                    └── (opcional) usuarios  (quién cambió el estado)
paquetes 1──N envios   (ya existente; no se altera)
```

### Notas de aplicación

1. Confirmar que `envios.id_envio` y `usuarios.id_usuario` sean `INT(11)` con el mismo signed/unsigned que las FKs nuevas (si no, error 150).
2. No ALTER de `envios` ni de columnas usadas por JPA.
3. Credenciales de MariaDB: solo en el XML de recursos del proyecto; **no** documentarlas aquí.
