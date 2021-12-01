package io.github.jonathanSampaio.domain.repository;

import io.github.jonathanSampaio.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
