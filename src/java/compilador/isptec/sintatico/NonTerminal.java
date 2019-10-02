/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.isptec.sintatico;
import compilador.isptec.lexico.*;
import java.util.ArrayList;

/**
 *
 * @author stacks
 */
public class NonTerminal extends Symbol{
    
    private final ArrayList<Derivation> derivations;
    
    public NonTerminal(String descrip) {
        super(descrip);
        derivations = new ArrayList<>();
    }
    
    public void addDerivation(Derivation d){
        this.derivations.add(d);
    }
    
    @Override
    public int checkSymbol(Token t){
        int c=0;
        for(Derivation d:derivations){
            if(d.checkDerivation(t)){
                return c;
            }
            c++;
        }
        return -1;
    }
    
    public Derivation getDerivation(int index){
        return derivations.get(index);
    }
    
}