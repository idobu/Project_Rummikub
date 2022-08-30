/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author home
 */
public class SparseMatrixNode {
    public int count;
    public Card NodeCard;


    
    public SparseMatrixNode()
    {
    }
    public SparseMatrixNode( Card card ,int count )
    {
        this.count = count;
        this.NodeCard=card; 
    }
    public SparseMatrixNode( SparseMatrixNode sp )
    {
        this.count = sp.count;
        this.NodeCard=sp.NodeCard;
        
    }

}
