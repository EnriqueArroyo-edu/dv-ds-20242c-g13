package ar.edu.davinci.dv_ds_20242c_g13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Cliente;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
