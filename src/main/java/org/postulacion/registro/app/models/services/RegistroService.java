package org.postulacion.registro.app.models.services;

import java.util.List;

import org.postulacion.registro.app.models.entity.Registro;

public interface RegistroService {
	
	public List<Registro> findAll();
	
	public Registro findById(Long id);
	
	public Registro save(Registro registro);
	
	public void deleteById(Long id);
}
