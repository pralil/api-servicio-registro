package org.postulacion.registro.app.models.dao;

import org.postulacion.registro.app.models.entity.Registro;
import org.springframework.data.repository.CrudRepository;

public interface RegistroDao extends CrudRepository<Registro, Long>{

}
