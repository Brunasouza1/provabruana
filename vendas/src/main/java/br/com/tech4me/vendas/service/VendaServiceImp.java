package br.com.tech4me.vendas.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.tech4me.vendas.httpClient.AtendimentoClient;
import br.com.tech4me.vendas.model.Venda;
import br.com.tech4me.vendas.repository.VendaRepository;
import br.com.tech4me.vendas.shared.VendaCompletoDto;
import br.com.tech4me.vendas.shared.VendaDto;

public class VendaServiceImp implements VendaService {

    @Autowired
    private VendaRepository repository;
    
    @Autowired
    private AtendimentoClient pizzariaClient;

    @Override
    public List<VendaCompletoDto> obterVenda() {
        List<Venda> venda = repository.findAll();
        List<VendaCompletoDto> dtoObter = venda.stream().map(p -> new ModelMapper().map(p, VendaCompletoDto.class)).collect(Collectors.toList());
        return dtoObter;
    }

    @Override
    public Optional<VendaDto> obterVendaPorId(String id) {
        Optional<Venda> venda = repository.findById(id);

        if(venda.isPresent()){
           VendaDto Vendaloja = new ModelMapper().map (venda, VendaDto.class);
           Vendaloja.setDadosAtendimento(pizzariaClient.obterAtendimento(Vendaloja.getIdAtendimento())); 
           pizzariaClient.obterAtendimento(id);
            return Optional.of(Vendaloja);
        }else{
            return Optional.empty();
        }
    }
    @Override
    public void excluirVenda(String id) {
        repository.deleteById(id);
        
    }

    @Override
    public VendaCompletoDto CadastrarVenda(VendaCompletoDto dtoObter) {
        Venda venda = new ModelMapper().map(dtoObter, Venda.class);

        repository.save(venda);
        return new ModelMapper().map(venda,VendaCompletoDto.class);
    }

    @Override
    public Optional<VendaDto> atualizarVendaPorId(String id, VendaDto dto) {
        Optional<Venda> retorno = repository.findById(id);

        if(retorno.isPresent()){
            Venda vendaRetorno = new ModelMapper().map(dto, Venda.class);
            vendaRetorno.setId(id);
            repository.save(vendaRetorno);
            return Optional.of(new ModelMapper().map(vendaRetorno, VendaDto.class));
        }else{
            return Optional.empty();
        }
    }   
    
}