package opiniones.rest;


import opiniones.model.Opinion;
import opiniones.model.Valoracion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpinionesRestClient {
	
	@POST("opiniones")
	Call<Void> createOpinion(@Body Opinion opinion);
	
	@GET("opiniones/{url}")
	Call<Opinion> getOpinion(@Path("url") String url);
	
	@DELETE("opiniones/{url}")
	Call<Void> removeOpinion(@Path("url") String url);
	
	@POST("opiniones/{url}")
	Call<Void> anadirValoracion(@Path("url") String url, @Body Valoracion valoracion);
	
	
}
