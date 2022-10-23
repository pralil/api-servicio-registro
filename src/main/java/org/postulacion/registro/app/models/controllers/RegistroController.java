package org.postulacion.registro.app.models.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.postulacion.registro.app.models.entity.Registro;
import org.postulacion.registro.app.models.services.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = {"http://localhost:8090"})
@RestController
@RequestMapping("/api")
public class RegistroController {
	
	@Autowired
	private RegistroService servicio;
	
	@GetMapping("/")
	public List<Registro> listar(){
		return servicio.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalle(@PathVariable Long id) {
		Registro registro = null;
		Map<String, Object> response = new HashMap<>();
		try {
			registro = servicio.findById(id);
			
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al consultar el registro en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(registro == null) {
			response.put("mensaje", "El registro con id: ".concat(id.toString().concat(" No existe en base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);  
		}
		
		return new ResponseEntity<Registro>(registro, HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> crear(@Valid @RequestBody Registro registro, BindingResult result) {
		Registro registroNuevo = new Registro();
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			registroNuevo = servicio.save(registro);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		response.put("mensaje", "El registro ha sido creado con éxito");
		response.put("registro", registroNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Registro registro, BindingResult result, @PathVariable Long id) {
		Registro registroActual = servicio.findById(id);
		
		Registro registroUpdate = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(registroActual == null) {
			response.put("mensaje", "Error, no existe el registro en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			registroActual.setNombre(registro.getNombre());
			registroActual.setPrecio(registro.getPrecio());
			registroActual.setFecha(new Date());
			
			registroUpdate = servicio.save(registroActual);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el registro en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El registro ha sido actualizado con éxito");
		response.put("registro", registroUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			servicio.deleteById(id);
			
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el registro en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El registro se elimino con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
	}

}
