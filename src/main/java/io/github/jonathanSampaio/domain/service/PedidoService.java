package io.github.jonathanSampaio.domain.service;

import io.github.jonathanSampaio.domain.entity.Pedido;
import io.github.jonathanSampaio.domain.enums.StatusPedido;
import io.github.jonathanSampaio.rest.dto.PedidoDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
    List<Pedido>buscarTodos();

    }

//    Boolean salvarPedido(PedidoDTO pedidoDTO);