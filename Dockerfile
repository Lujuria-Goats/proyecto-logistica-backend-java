# ==========================================
# ETAPA 1: BUILDER (Compilación)
# ==========================================
# Usamos una imagen que ya tiene Maven y Java JDK listos
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Establecemos el directorio de trabajo dentro del "Taller"
WORKDIR /app

# 1. Copiamos SOLO el pom.xml primero (Estrategia de Caché)
# Esto hace que Docker guarde las dependencias en memoria. Si no cambias el pom,
# las siguientes veces no tendrá que descargar todo internet de nuevo.
COPY pom.xml .

# 2. Descargamos las dependencias (sin copiar el código aún)
RUN mvn dependency:go-offline

# 3. Ahora sí, copiamos el código fuente (src)
COPY src ./src

# 4. Compilamos y empaquetamos
# -DskipTests: Saltamos los tests para que el build sea rápido en la VPS
# (Idealmente los tests se corren antes, pero para el MVP esto asegura que compile)
RUN mvn clean package -DskipTests

# ==========================================
# ETAPA 2: RUNTIME (Ejecución)
# ==========================================
# Usamos una imagen ligera (Alpine) que solo tiene lo necesario para CORRER Java
FROM eclipse-temurin:17-jre-alpine

# Definimos zona horaria (Opcional, pero útil para logs en Colombia)
ENV TZ=America/Bogota

# Directorio de trabajo en la imagen final
WORKDIR /app

# Copiamos SOLO el archivo .jar generado en la ETAPA 1 (builder)
# Lo renombramos a "app.jar" para que sea fácil de ejecutar
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]