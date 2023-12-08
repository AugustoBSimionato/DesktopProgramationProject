package Model;

/**
 *
 * @author augustosimionato
 */
public class Order {
    
    private int idOrder;
    private String nomeCompleto;
    private String endereco;
    private String cep;
    private String descricaoPedido;
    private String tipoPedido;
    
    public Order(int io, String nc, String end, String ce, String des, String tip) {
        this.idOrder = io;
        this.nomeCompleto = nc;
        this.endereco = end;
        this.cep = ce;
        this.descricaoPedido = des;
        this.tipoPedido = tip;
    }

    @Override
    public String toString() {
        if (this != null) {
            return (nomeCompleto + "  " + endereco + "  " + cep + "  " + descricaoPedido + "  " + tipoPedido);
        } else {
            return (null);
        }
    }

    /**
     * @return the nomeCompleto
     */
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    /**
     * @param nomeCompleto the nomeCompleto to set
     */
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the descricao
     */
    public String getDescricaoPedido() {
        return descricaoPedido;
    }

    /**
     * @param descricaoPedido the descricao to set
     */
    public void setDescricaoPedido(String descricaoPedido) {
        this.descricaoPedido = descricaoPedido;
    }

    /**
     * @return the tipoPedido
     */
    public String getTipoPedido() {
        return tipoPedido;
    }

    /**
     * @param tipoPedido the tipoPedido to set
     */
    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    /**
     * @return the idOrder
     */
    public int getIdOrder() {
        return idOrder;
    }

    /**
     * @param idOrder the idOrder to set
     */
    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }
}
