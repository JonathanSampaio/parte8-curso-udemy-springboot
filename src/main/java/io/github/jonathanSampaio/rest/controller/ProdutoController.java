package io.github.jonathanSampaio.rest.controller;


import io.github.jonathanSampaio.domain.entity.Cliente;
import io.github.jonathanSampaio.domain.entity.Produto;
import io.github.jonathanSampaio.domain.repository.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {


    private ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("{id}")
    public Produto getProdutoById(@PathVariable("id") Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ,"Produto não encontrado") );
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Produto save(@RequestBody @Valid Produto produto){
        return produtoRepository.save(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public Produto delete( @PathVariable("id") Integer id ){
        return produtoRepository
                .findById(id)
                .map( produto  -> { produtoRepository.delete(produto) ;
                    return produto; })
                .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ,"Cliente não encontrado") );
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Integer id,
                       @RequestBody @Valid Produto produto){
        produtoRepository
                .findById(id)
                .map( produtoExistente -> {
                    produto.setId(produtoExistente.getId());
                    produtoRepository.save(produto);
                    return produtoExistente;
                }).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ,"Cliente não encontrado")) ;
    }

    @GetMapping
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING );
        Example example = Example.of(filtro, matcher);
        List<Produto> lista = produtoRepository.findAll(example);

        return produtoRepository.findAll(example);
    }
}
