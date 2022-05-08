using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace Guias.Modelo
{
    public class Guia
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string Nombre { get; set; }
        public string Email { get; set; }
        public List<string> UrlSitioInteres { get; set; }

        public List<ResumenValoracion> Valoraciones { get; set; } = new List<ResumenValoracion>();
    }

    public class ResumenValoracion {
        public string Comentario { get; set; }
        public string Email { get; set; }
        public double Calificacion { get; set; }
    }
}