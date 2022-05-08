using Guias.Modelo;
using Guias.Servicio;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;

namespace Guias.Controllers
{
    [Route("api/guias")]
    [ApiController]
    public class GuiasController : ControllerBase
    {
        private readonly IServicioGuias _servicio;

        public GuiasController(IServicioGuias servicio)
        {
            _servicio = servicio;
        }

        [HttpGet]
        public ActionResult<List<Guia>> Get() => _servicio.getAll();

        [HttpGet("{id}", Name = "GetGuia")]
        public ActionResult<Guia> Get(string id)
        {
            var entidad = _servicio.Get(id);

            if (entidad == null)
            {
                return NotFound();
            }

            return entidad;
        }

        [HttpPost]
        public ActionResult<Guia> Create(Guia guia)
        {
            _servicio.Create(guia);

            return CreatedAtRoute("GetActividad", new { id = guia.Id }, guia);
        }

        [HttpPut("{id}")]
        public IActionResult Update(string id, Guia guia)
        {
            var entidad = _servicio.Get(id);

            if (entidad == null)
            {
                return NotFound();
            }

            _servicio.Update(guia);

            return NoContent();
        }

        [HttpDelete("{id}")]
        public IActionResult Delete(string id)
        {
            var guia = _servicio.Get(id);

            if (guia == null)
            {
                return NotFound();
            }

            _servicio.Remove(id);

            return NoContent();
        }

        [HttpDelete("{id}/sitios/{urlSitioInteres}")]
        public IActionResult DeleteSitioInteres(string id, string urlSitioInteres)
        {
            var guia = _servicio.Get(id);

            if (guia == null)
            {
                return NotFound();
            }

            bool ExisteUrl = _servicio.DeleteSitioInteres(id, urlSitioInteres);

            if (ExisteUrl)
            {
                return NoContent();
            }
            else
            {
                return NotFound();
            }

        }

        [HttpGet("sitios/{urlSitioInteres}/guias")]
        public ActionResult<List<Guia>> GetBySitioInteres(string urlSitioInteres)
        {
            var guias = _servicio.GetBySitioInteres(urlSitioInteres);

            if (guias == null)
            {
                return NotFound();
            }

            return guias;

        }
    }
}