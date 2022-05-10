package opiniones.repositorio;

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

import opiniones.model.Opinion;

public class RepositorioOpinionesMongoDB implements RepositorioOpiniones {

	private MongoCollection<Opinion> opiniones;

	public RepositorioOpinionesMongoDB() {
		String uriString = "mongodb://arso:arso-22@cluster0-shard-00-00.tn7mq.mongodb.net:27017,cluster0-shard-00-01.tn7mq.mongodb.net:27017,cluster0-shard-00-02.tn7mq.mongodb.net:27017/myFirstDatabase?ssl=true&replicaSet=atlas-aoo8qh-shard-0&authSource=admin&retryWrites=true&w=majority";

		ConnectionString connectionString = new ConnectionString(uriString);

		CodecRegistry pojoCodecRegistry = CodecRegistries
				.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				pojoCodecRegistry);
		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.codecRegistry(codecRegistry).build();

		MongoClient mongoClient = MongoClients.create(clientSettings);

		MongoDatabase db = mongoClient.getDatabase("arso");

		this.opiniones = db.getCollection("opiniones", Opinion.class);
	}

	/** Métodos de apoyo **/

	protected boolean checkDocument(ObjectId id) {

		Opinion opinion = opiniones.find(Filters.eq("_id", id)).first();

		return opinion != null;
	}
	
	protected boolean checkUrlRepetida(String url) {
		Opinion opinion = opiniones.find(Filters.eq("url", url)).first();
		
		if (opinion != null) return true;
		else return false;
	}

	/** fin métodos de apoyo **/
	
	@Override
	public boolean isUrlRepetida(String url) {
		return checkUrlRepetida(url);
	}
	@Override
	public String add(Opinion opinion) throws RepositorioException {
		try {

			ObjectId id = new ObjectId();
			opinion.setId(id);

			opiniones.insertOne(opinion);

			return id.toString();

		} catch (Exception e) {
			throw new RepositorioException("No se ha podido insertar la entidad", e);
		}
	}

	@Override
	public void update(Opinion opinion) throws RepositorioException, EntidadNoEncontrada {
		if (!checkDocument(opinion.getId())) {
			throw new EntidadNoEncontrada("No existe la entidad con id:" + opinion.getId());
		}
		try {
			opiniones.replaceOne(Filters.eq("_id", opinion.getId()), opinion);

		} catch (Exception e) {
			throw new RepositorioException("No se ha podido actualizar la entidad, id:" + opinion.getId(), e);
		}
	}

	@Override
	public void delete(Opinion opinion) throws RepositorioException, EntidadNoEncontrada {
		if (!checkDocument(opinion.getId()))
			throw new EntidadNoEncontrada("No existe la entidad con id:" + opinion.getId());

		try {
			opiniones.deleteOne(Filters.eq("_id", opinion.getId()));

		} catch (Exception e) {
			throw new RepositorioException("No se ha podido borrar la entidad, id:" + opinion.getId(), e);
		}

	}

	@Override
	public Opinion getById(String id) throws RepositorioException, EntidadNoEncontrada {
		Opinion opinion = opiniones.find(Filters.eq("_id", new ObjectId(id))).first();

		if (opinion == null)
			throw new EntidadNoEncontrada("No existe la entidad con id:" + id);

		return opinion;
	}

	public Opinion getByUrl(String url) throws EntidadNoEncontrada  {
		Opinion opinion = opiniones.find(Filters.eq("url", new String(url))).first();

		if (opinion == null)
			throw new EntidadNoEncontrada("No existe la entidad con url:" + url);

		return opinion;
	}

	@Override
	public List<Opinion> getAll() throws RepositorioException {
		LinkedList<Opinion> allOpiniones = new LinkedList<>();

		opiniones.find().into(allOpiniones);

		return allOpiniones;
	}

	@Override
	public List<String> getIds() {
		LinkedList<String> allIds = new LinkedList<String>();

		opiniones.find().map(e -> e.getId().toString()).into(allIds);

		return allIds;
	}

}
