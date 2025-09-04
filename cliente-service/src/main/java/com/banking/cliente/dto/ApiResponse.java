package com.banking.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(description = "Respuesta estándar de la API con información de éxito/error y datos")
public class ApiResponse<T> {
    
    @Schema(
        description = "Indica si la operación fue exitosa", 
        example = "true"
    )
    @JsonProperty("success")
    private boolean success;
    
    @Schema(
        description = "Mensaje descriptivo de la operación", 
        example = "Cliente creado exitosamente"
    )
    @JsonProperty("message")
    private String message;
    
    @Schema(
        description = "Datos de respuesta (puede ser null en caso de error)"
    )
    @JsonProperty("data")
    private T data;
    
    // Constructors
    public ApiResponse() {}
    
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Static factory methods para crear respuestas comunes
    @Schema(hidden = true)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    @Schema(hidden = true)
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    @Schema(hidden = true)
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }
    
    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}