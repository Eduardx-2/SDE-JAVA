
### Objetivo

- Crear el módulo encargado de registrar, consultar, modificar y eliminar usuarios del sistema.

**Java - Usuarios:**

GESTIÓN DE USUARIOS

- [x] ├── Registrar usuario
- [ ] ├── Listar usuarios
- [ ] ├── Buscar usuario
- [ ] ├── Actualizar usuario
- [ ] └── Eliminar usuario
### Registrar

El sistema debe permitir crear un usuario con:

- Nombre
- Apellido
- Correo
- Contraseña
- Rol

## Gestion de usuarios

- La contraseña: se esta manejando con el uso de Console, y su función readPassword().


Encriptación: 

> 	- PBKDF2 (Password-Based Key Derivation Function 2) es una función criptográfica que convierte una contraseña en una clave de cifrado segura mediante un proceso intencionalmente lento para resistir ataques de fuerza bruta.  Su funcionamiento se basa en aplicar repetidamente una función pseudoaleatoria (generalmente HMAC-SHA256) a la contraseña combinada con un valor aleatorio único llamado sal. 
> 		- Esto es un proceso unidireccional, por ende, así vulneren la base de datos no van a poder conseguir las contraseñas

## Usuario

# Base de datos

| Campo        | Tipo    | Descripción            |
| ------------ | ------- | ---------------------- |
| `id_usuario` | INT PK  | Identificador          |
| `nombre`     | VARCHAR | Nombre                 |
| `apellido`   | VARCHAR | Apellido               |
| `correo`     | VARCHAR | Correo electrónico     |
| `telefono`   | VARCHAR | Teléfono               |
| `password`   | TEXT    | Contraseña             |

> [!NOTE]
> Con este codigo, se crea la base de datos.
> 
>`CREATE TABLE usuarios (`
>     `id_usuario INT AUTO_INCREMENT PRIMARY KEY,`
>     `nombre VARCHAR(60) NOT NULL,`
>     `apellido VARCHAR(60) NOT NULL,`
>     `correo VARCHAR(70) NOT NULL UNIQUE,`
>     `telefono VARCHAR(20) UNIQUE NOT NULL,`
>     `password TEXT NOT NULL,`
>     `rol VARCHAR(10) NOT NULL`
> `);`



## Paquetes  

## 1. Gestión de Paquetes

**Objetivo:** permitir registrar y administrar los paquetes que serán enviados.

## Tabla de Paquetes

| Campo         | Descripción                     |
| ------------- | ------------------------------- |
| `id_paquete`  | Identificador único             |
| `codigo`      | Código único del paquete        |
| `descripcion` | Descripción del contenido       |
| `peso`        | Peso del paquete                |
| `largo`       | Largo                           |
| `ancho`       | Ancho                           |
| `alto`        | Alto                            |
| `valor`       | Valor declarado                 |
| `id_usuario`  | Usuario que registra el paquete |

| Campo         | Tipo de dato    | Restricciones                | Descripción                                               |
| ------------- | --------------- | ---------------------------- | --------------------------------------------------------- |
| `id_paquete`  | `INT`           | `PRIMARY KEY AUTO_INCREMENT` | Identificador interno único                               |
| `codigo`      | `VARCHAR(30)`   | `UNIQUE NOT NULL`            | Código único del paquete, puede contener letras y números |
| `descripcion` | `VARCHAR(255)`  | `NOT NULL`                   | Descripción del contenido                                 |
| `peso`        | `DECIMAL(8,2)`  | `NOT NULL`                   | Peso del paquete, por ejemplo `2.50` kg                   |
| `largo`       | `DECIMAL(8,2)`  | `NOT NULL`                   | Largo del paquete                                         |
| `ancho`       | `DECIMAL(8,2)`  | `NOT NULL`                   | Ancho del paquete                                         |
| `alto`        | `DECIMAL(8,2)`  | `NOT NULL`                   | Alto del paquete                                          |
| `valor`       | `DECIMAL(10,2)` | `NOT NULL`                   | Valor declarado del paquete                               |
| `id_usuario`  | `INT`           | `NOT NULL, FOREIGN KEY`      | Usuario que registra el paquete                           |
|               |                 |                              |                                                           |

> [!Codigo tabla de datos paquetes]
```
> CREATE TABLE paquetes (
>     id_paquete INT PRIMARY KEY AUTO_INCREMENT,
>     codigo VARCHAR(30) NOT NULL UNIQUE,
>     descripcion VARCHAR(255),
>     peso DECIMAL(8,3) NOT NULL,
>     largo DECIMAL(8,2) NOT NULL,
>     ancho DECIMAL(8,2) NOT NULL,
>     alto DECIMAL(8,2) NOT NULL,
>     valor DECIMAL(10,2) NOT NULL,
>     id_usuario INT NOT NULL,
>     
>     FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
> );
```

## Base de datos - Conceptos

> [!NOTE]
> Una **foreign key** (clave externa) en SQL es una columna o conjunto de columnas en una tabla (tabla hija) que establece una relación con la **primary key** (clave primaria) de otra tabla (tabla padre), garantizando la **integridad referencial** de la base de datos.

Explicación: https://www.w3schools.com/sql/sql_foreignkey.asp
## Java -> JPA Clase PaquetesData

```
private String codigo;
    private String descrip;
    private float largo;
    private float peso;
    private float ancho;
    private float alto;
    private float valor;
```

cada elemento tiene un getter y setter, esta clase va ser relacionada con JPA, al momento de consultar a la base de datos, va realizar un mapeo.

## JPA 

`@Entity`
`@Table(name = "usuarios", schema = "paquetes_system")`
`public class Usuarios implements Serializable {`
    `@Id`
    `@GeneratedValue(strategy = GenerationType.IDENTITY)`
    `private int id_usuario;`
    `@Column(name = "nombre")`
    `private String nombre;`
    `@Column(name = "apellido")`
    `private String apellido;`
    `@Column(name="correo")`
    `private String correo;`
    `@Column(name="telefono")`
    `private String telefono;`
    `@Column(name="password")`
    `private String pass;`

En mi clase Usuarios, suceden dos cosas; primeramente tiene un constructor el cuál recibe todos estos parametros que se pueden apreciar en el codigo, pero, también se necesita un constructor vacio:

> [!CONSTRUCTOR VACIO]
> <span style="color:rgb(255, 0, 0)">EclipseLink necesita poder crear primero la instancia mediante reflexión, y después rellenar sus atributos desde la base de datos.</span>

> [!JPQL PERSISTENCE.XML]
> **JPQL** (**Java Persistence Query Language**), ahora conocido como **Jakarta Persistence Query Language**, es un lenguaje de consultas orientado a objetos y **independiente de la plataforma** definido como parte de la especificación **Java Persistence API (JPA)**.

## Rol de usuario:

> [!CODIGO ROL]
```
> public enum Roles {
>     ADMIN(1,"ADMIN"),
>     USER(2,"USER"),
>     CLIENTE(3,"CLIENTE");
>     
>     private final int role;
>     private final String value;
```
`Enum` que mapee de forma estricta los valores enteros permitidos.

```
public static Roles verification_asset(int co){
        for(Roles rol: Roles.values()){
            if(rol.role == co){
                return rol;
            }
        }
        throw new IllegalArgumentException("[-] USTED INGRESO UN ROL EQUIVOCADO");
    }
```

# persistence.xml

> [!NOTE]
> <properties>
>       <property name="eclipselink.logging.level" value="FINE"/>
>       <property name="jakarta.persistence.jdbc.url" value="jdbc:mariadb://localhost:3306/paquetes_system"/>
>       <property name="jakarta.persistence.jdbc.user" value="$usuario"/>
>       <property name="jakarta.persistence.jdbc.driver" value="org.mariadb.jdbc.Driver"/>
>       <property name="jakarta.persistence.jdbc.password" value=""/>
>     </properties>

	Jakarta: un **conjunto de especificaciones** (APIs) que extienden Java SE para permitir la creación de aplicaciones distribuidas, escalables y de alto rendimiento.
# Funciones

GESTIÓN DE PAQUETES

- [x] ├── Registrar paquete
- [x] ├── Listar paquetes
- [x] ├── Buscar paquete
- [ ] ├── Actualizar paquete
- [ ] └── Eliminar paquete

## USUARIO

GESTIÓN DE MIS PAQUETES

- [x] ├── Registrar paquete
- [x] ├── Ver mis paquetes
- [x] ├── Buscar mis paquetes
- [ ] └── Ver paquete por ID


