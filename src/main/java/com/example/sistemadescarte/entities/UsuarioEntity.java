package com.example.sistemadescarte.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_usuarios")
@Getter
@Setter
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String usuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private boolean ativo = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permissoes_id")
    @JsonIgnore
    private PermissoesEntity permissoes;

    // Compatibilidade com o formulario atual.
    // Este campo nao existe mais como coluna no banco.
    @Transient
    private String permissao;

    public String getPermissao() {
        if (permissoes != null) {
            return permissoes.getNome();
        }
        return permissao;
    }

    @JsonProperty("permissaoId")
    public Integer getPermissaoId() {
        return permissoes == null ? null : permissoes.getId();
    }
}