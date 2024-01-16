package mx.ipn.escom.alcantarae.proymoviles.apihandler

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiServiceInterface {

    @HTTP(method = "CUSTOM", path = "/verificar_usuario/{nombre}/{contrasena}")
    suspend fun verificarUsuario(
        @Path("nombre") nombre: String,
        @Path("contrasena") contrasena: String
    ): Response<verUserResponse>

    @POST("/usuarios/{nombre}/{contrasena}/{rol}")
    suspend fun registrarUsuario(
        @Path("nombre") nombre: String,
        @Path("contrasena") contrasena: String,
        @Path("rol") rol: String
    ):Response<Int>

    @GET("/catalogo")
    suspend fun regresasCatalogo(): List<Producto>

    @POST("/pedidos/{idusuario}/{idrepartidor}/{idproductos}")
    suspend fun nuevoPedido(
        @Path("idusuario") idusuario: Int,
        @Path("idrepartidor") idrepartidor: Int,
        @Path("idproductos") idproductos: Int
        ):Response<Int>

    @GET("/historia_pedido/{idusuario}")
    suspend fun historialUsuario(
        @Path("idusuario") idusuario: Int
    ):List<Pedido>

    @GET("/historia_repartidor/{idusuario}")
    suspend fun historialRepartidor(
        @Path("idusuario") idusuario: Int
    ):List<Pedido>

    @GET("/pedidos/{idpedido}")
    suspend fun infoPedido(
        @Path("idpedido") idpedido: Int
    ):Response<Pedido>

    @PUT("/pedidos/{idpedido}/{idrepartidor}")
    suspend fun actualizarRepartidor(
        @Path("idpedido") idpedido: Int,
        @Path("idrepartidor") idrepartidor: Int
    ):Response<Void>

    //actualizar_estado
    @HTTP(method = "CUSTOM", path = "/actualizar_estado/{idPedido}/{latitud}/{longitud}")
    suspend fun actualizar_localizacion(
        @Path("idPedido") idpedido: Int,
        @Path("latitud") latitud: Float,
        @Path("longitud") longitud: Float
    ): Response<Void>

    @HTTP(method = "CUSTOM", path = "/actualizar_estado_c/{idPedido}/{latitudC}/{longitudC}")
    suspend fun actualizar_localizacionC(
        @Path("idPedido") idpedido: Int,
        @Path("latitudC") latitudC: Float,
        @Path("longitudC") longitudC: Float
    ): Response<Void>

    @POST
    suspend fun nuevoPedido(
        @Url url: String
    ): Response<Int>

    @PUT("/Estado_en/{idPedido}")
    suspend fun terminarPedido(
        @Path("idPedido") idpedido: Int
    ): Response<Void>
}

data class verUserResponse (
    val id: Int,
    val usuario: String,
    val contraseña: String,
    val rol: String
)
data class Producto(
    val id: Int,
    val nombre_producto: String,
    val imagenes: String?,
    val precio: Double,
    val categoria: String
)
data class Pedido(
    val id_pedido: Int,
    val id_usuario: Int,
    val id_repartidor: Int,
    val latitud: Float,
    val longitud: Float,
    val Estado: String,
    val latitudC: Float,
    val longitudC: Float,
    val productos: String
)

