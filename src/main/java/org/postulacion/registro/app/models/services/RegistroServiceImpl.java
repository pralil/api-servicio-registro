package org.postulacion.registro.app.models.services;

import java.util.List;

import org.postulacion.registro.app.models.dao.RegistroDao;
import org.postulacion.registro.app.models.entity.Registro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroServiceImpl implements RegistroService{

	@Autowired
	private RegistroDao registroDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Registro> findAll() {
		return (List<Registro>) registroDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Registro findById(Long id) {
		return registroDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Registro save(Registro registro) {
		return registroDao.save(registro);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		registroDao.deleteById(id);
	}

	
}
