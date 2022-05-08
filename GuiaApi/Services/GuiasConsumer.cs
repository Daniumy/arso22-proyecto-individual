using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Text;
using Guias.Repositorio;
using Newtonsoft.Json;
using System.Collections.Generic;
using Guias.Modelo;

namespace Guias.Consumer
{
    public class Receive
    {
        private RepositorioGuias repositorio;
        public void consumir()
        {
            Console.WriteLine("he entrado en el main del consumer");
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
                                Console.WriteLine(id);
                                Guia guia = repositorio.GetById(id);
                                Console.WriteLine(guia);
                                if (guia != null)
                                {
                                    Console.WriteLine("Hola");
                                    Console.WriteLine(guia.Nombre);
                                }
                                channel.BasicAck(ea.DeliveryTag, false);
                                //repositorio.GetById(Encoding.UTF8.GetString(body));
                                
                            };
            // this consumer tag identifies the subscription
            // when it has to be cancelled
            string consumerTag = channel.BasicConsume(queueName, false, consumer);
        }
    }
}