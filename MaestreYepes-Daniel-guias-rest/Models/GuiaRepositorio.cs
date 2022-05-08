using Repositorio;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;
using MongoDB.Bson;
using Guias.Modelo;

namespace Guias.Repositorio
{
     public interface RepositorioGuias : Repositorio<Guia, string>
    {
        public bool DeleteSitioInteres(string idGuia, string urlSitioInteres) {
            Guia guia = GetById(idGuia);
            bool ExisteUrl = guia.UrlSitioInteres.Remove(urlSitioInteres);
            if (ExisteUrl) {
                Update(guia);
                return true;
            }
            return false;
        }

        public List<Guia> GetBySitioInteres(string urlSitioInteres) {
            return GetAll().Where(x => x.UrlSitioInteres.Contains(urlSitioInteres)).ToList();
        }

    }

    public class RepositorioGuiasMongoDB : RepositorioGuias
    {

        
        private readonly IMongoCollection<Guia> guias;

        public RepositorioGuiasMongoDB()
        {
            var client = new MongoClient("mongodb+srv://arso:arso-22@cluster0.tn7mq.mongodb.net/arso?retryWrites=true&w=majority");
            var database = client.GetDatabase("arso");
            guias = database.GetCollection<Guia>("guias");
        }

        public string Add(Guia entity)
        {
            guias.InsertOne(entity);

            return entity.Id;
        }

        public void Update(Guia entity)
        {
            guias.ReplaceOne(guia => guia.Id == entity.Id, entity);
        }

        public void Delete(Guia entity)
        {
            guias.DeleteOne(guia => guia.Id == entity.Id);
        }

        public List<Guia> GetAll()
        {
            return guias.Find(_ => true).ToList();
        }

        public Guia GetById(string id)
        {
            return guias
                .Find(guia => guia.Id == id)
                .FirstOrDefault();
        }

        public List<string> GetIds()
        {
            var todas =  guias.Find(_ => true).ToList();

            return todas.Select(p => p.Id).ToList();

        }
    }
}