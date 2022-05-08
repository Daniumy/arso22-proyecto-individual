
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Guias.Repositorio;
using Guias.Servicio;
using Guias.Consumer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;

namespace GuiaApi
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddSingleton<RepositorioGuias,RepositorioGuiasMongoDB>(); 
            services.AddSingleton<IServicioGuias,ServicioGuias>(); 
            services.AddSingleton<Receive>();
            
            services.AddControllers(options =>
            {
                options.Filters.Add(typeof(ManejadorGlobalErrores));
            });

            services.AddControllers();
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "GuiaApi", Version = "v1" });
            });
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "GuiaApi v1"));
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}

public class ManejadorGlobalErrores : ExceptionFilterAttribute
{
    public override void OnException(ExceptionContext context)
    {
        base.OnException(context);

        if (context.Exception is ArgumentException) {
            context.Result = new BadRequestResult();
        }         
    }
}
