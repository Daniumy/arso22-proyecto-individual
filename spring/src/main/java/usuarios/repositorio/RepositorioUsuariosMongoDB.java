package usuarios.repositorio;

import java.util.LinkedList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import usuarios.model.Usuario;

public class RepositorioUsuariosMongoDB implements RepositorioUsuarios {

	private MongoCollection<Usuario> usuarios;

	public RepositorioUsuariosMongoDB() {
		String uriString = "mongodb://arso:arso-22@cluster0-shard-00-00.tn7mq.mongodb.net:27017,cluster0-shard-00-01.tn7mq.mongodb.net:27017,cluster0-shard-00-02.tn7mq.mongodb.net:27017/arso?ssl=true&replicaSet=atlas-aoo8qh-shard-0&authSource=admin&retryWrites=true&w=majority";

		ConnectionString connectionString = new ConnectionString(uriString);

		CodecRegistry pojoCodecRegistry = CodecRegistries
				.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				pojoCodecRegistry);
		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.codecRegistry(codecRegistry).build();

		MongoClient mongoClient = MongoClients.create(clientSettings);

		MongoDatabase db = mongoClient.getDatabase("arso");

		this.usuarios = db.getCollection("usuarios", Usuario.class);
	}

	/** Métodos de apoyo **/

	protected boolean checkDocument(ObjectId id) {
		
		Usuario usuario = usuarios.find(Filters.eq("_id", id)).first();

		return usuario != null;
	}

	/** fin métodos de apoyo **/

	@Override
	public Usuario getById(String id) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = usuarios.find(Filters.eq("userId", new String(id))).first();
		
		if (usuario == null)
			throw new EntidadNoEncontrada("No existe la entidad con id:" + id);

		return usuario;
	}

	@Override
	public List<Usuario> getAll() throws RepositorioException {
		LinkedList<Usuario> allOpiniones = new LinkedList<>();

		usuarios.find().into(allOpiniones);

		return allOpiniones;
	}

	@Override
	public List<String> getIds() {
		LinkedList<String> allIds = new LinkedList<String>();

		usuarios.find().map(e -> e.getUserId().toString()).into(allIds);

		return allIds;
	}


}
