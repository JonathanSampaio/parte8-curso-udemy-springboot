package io.github.jonathanSampaio.domain.repository;

import io.github.jonathanSampaio.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query(value = " select * " +
            " from cliente c " +
            " where c.nome like '%:nome%' ", nativeQuery = true)
    List<Cliente> encontrarPorNome (@Param("nome") String nome);

    @Query(" delete from Cliente c where c.nome = :nome ")
    @Modifying
    void deleteByNome(String nome);

//    List<Cliente> findByNomeLike(String nome);

    boolean existsByNome(String nome);

    @Query(value = " SELECT c FROM Cliente c " +
            " LEFT JOIN FETCH c.pedidos " +
            " WHERE c.id = :id ")
    Cliente findClienteFetchPedidos(@Param("id") Integer id );


}
