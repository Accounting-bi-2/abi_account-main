# Stage 1: Build the native image using GraalVM
FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-22 AS builder
WORKDIR /home/app
COPY . /home/app

# Install necessary tools and dependencies for GraalVM native image
RUN gu install native-image

# Compile the native image using Gradle
RUN ./gradlew nativeCompile

# Stage 2: Create the final Docker image
FROM oraclelinux:8-slim

# Install required dependencies
RUN microdnf install -y libstdc++

# Set up a directory for the application
WORKDIR /app

# Copy the native executable from the first stage build
COPY --from=builder /home/app/build/native/nativeCompile/abi_accounts /app/

# Ensure the binary is executable
RUN chmod +x /app/abi_accounts

# Expose the application's port
EXPOSE 8080

# Command to run the executable
CMD ["./abi_accounts"]

