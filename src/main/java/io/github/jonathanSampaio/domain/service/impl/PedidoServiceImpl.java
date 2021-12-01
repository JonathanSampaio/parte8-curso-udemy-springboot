package io.github.jonathanSampaio.domain.service.impl;

import io.github.jonathanSampaio.domain.entity.Cliente;
import io.github.jonathanSampaio.domain.entity.ItemPedido;
import io.github.jonathanSampaio.domain.entity.Pedido;
import io.github.jonathanSampaio.domain.entity.Produto;
import io.github.jonathanSampaio.domain.enums.StatusPedido;
import io.github.jonathanSampaio.domain.repository.ClienteRepository;
import io.github.jonathanSampaio.domain.repository.ItemPedidoRepository;
import io.github.jonathanSampaio.domain.repository.PedidoRepository;
import io.github.jonathanSampaio.domain.repository.ProdutoRepository;
import io.github.jonathanSampaio.domain.service.PedidoService;
import io.github.jonathanSampaio.domain.service.exception.PedidoNaoEncontradoException;
import io.github.jonathanSampaio.exception.RegraNegocioException;
import io.github.jonathanSampaio.rest.dto.ItemPedidoDTO;
import io.github.jonathanSampaio.rest.dto.PedidoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getIdCliente();
        Cliente cliente =  clienteRepository
                .findById(idCliente)
                .orElseThrow( () -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido>itensPedido = converterItens(pedido, dto.getItens());
        pedidoRepository.save(pedido);

        itemPedidoRepository.saveAll(itensPedido);

        pedido.setItens(itensPedido);
        return pedido;
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens){
        if (itens.isEmpty()){
            throw  new RegraNegocioException("Não é possível realizar um pedido sem itens.");
        }
        return itens
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getIdProduto();
                    Produto produto = produtoRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: " + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }

    public Boolean salvarPedido(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDTO.getTotal());
        pedidoRepository.save(pedido);

        return true;
    }
    public List<Pedido>buscarTodos(){
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidoRepository.save(pedido);
                }).orElseThrow( () -> new PedidoNaoEncontradoException());
    }


}
