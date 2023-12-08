package Model;

/**
 * @author augustosimionato
 */
public class Pizza {
    
    private int idPizza;
    private String nomePizza;
    private String descricao;

    public Pizza(int ip, String np, String des) {
        this.idPizza = ip;
        this.nomePizza = np;
        this.descricao = des;
    }

    @Override
    public String toString() {
        if (this != null) {
            return (nomePizza + "  " + descricao);
        } else {
            return (null);
        }
    }

    /**
     * @return the nomePizza
     */
    public String getNomePizza() {
        return nomePizza;
    }

    /**
     * @param nomePizza the nomePizza to set
     */
    public void setNomePizza(String nomePizza) {
        this.nomePizza = nomePizza;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the idPizza
     */
    public int getIdPizza() {
        return idPizza;
    }

    /**
     * @param idPizza the idPizza to set
     */
    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }
}
