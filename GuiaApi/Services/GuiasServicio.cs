using System;
using System.Text;
using System.Collections.Generic;
using Guias.Modelo;
using Guias.Repositorio;
using Guias.Consumer;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using Newtonsoft.Json;
namespace Guias.Servicio

{

    public interface IServicioGuias
    {

        string Create(Guia guia);

        void Update(Guia guia);

        Guia Get(string id);

        void Remove(string id);
        List<Guia> getAll();

        bool DeleteSitioInteres(string idGuia, string urlSitioInteres);

        List<Guia> GetBySitioInteres(string urlSitioInteres);
    }

    public class ServicioGuias : IServicioGuias
    {

        private RepositorioGuias repositorio;

        public ServicioGuias(RepositorioGuias repositorio)
        {
            this.repositorio = repositorio;

            ConnectionFactory factory = new ConnectionFactory();
            factory.Uri = new Uri("amqps://wswcdlhl:pIJYGbkfqu-TYpyC0tKcDwEt-xQVTH6K@squid.rmq.cloudamqp.com/wswcdlhl");
            IConnection conn = factory.CreateConnection();

            IModel channel = conn.CreateModel();
            string queueName = "arso-queue-guias";
            channel.QueueDeclare(queueName, true, false, false, null);
            channel.QueueBind(queueName, "arso-exchange", "valoracionGuia", null);

            var consumer = new EventingBasicConsumer(channel);
            consumer.Received += (ch, ea) =>
                            {
                                var body = ea.Body.ToArray();
                                string evento = Encoding.UTF8.GetString(body);
                                dynamic json = JsonConvert.DeserializeObject(evento);
                                string url = json.url;
                                Console.WriteLine(json);
                                string id = url.Split('/')[3];
                                Guia guia = repositorio.GetById(id);
                                if (guia != null)
                                {
                                    ResumenValoracion resumen = new ResumenValoracion();
                                    resumen.Comentario = json.valoracion.comentario;
                                    resumen.Calificacion = json.valoracion.calificacion;
                                    resumen.Email = json.valoracion.email;
                                    guia.Valoraciones.Add(resumen);
                                    repositorio.Update(guia);
                                }
                                channel.BasicAck(ea.DeliveryTag, false);
                            };
            string consumerTag = channel.BasicConsume(queueName, false, consumer);
        }


        public String Create(Guia guia)
        {
            return repositorio.Add(guia);

        }

        public void Update(Guia guia)
        {

            repositorio.Update(guia);
        }

        public Guia Get(String id)
        {
            return repositorio.GetById(id);
        }

        public void Remove(String id)
        {

            Guia guia = repositorio.GetById(id);

            repositorio.Delete(guia);
        }

        public List<Guia> getAll()
        {
            return repositorio.GetAll();
        }

        public bool DeleteSitioInteres(string idGuia, string urlSitioInteres)
        {
            return repositorio.DeleteSitioInteres(idGuia, urlSitioInteres);
        }

        public List<Guia> GetBySitioInteres(string urlSitioInteres)
        {
            return repositorio.GetBySitioInteres(urlSitioInteres);
        }
    }


}