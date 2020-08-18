/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.jsontree;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arpablue.jsontree.JSONtree;
/**
 *
 * @author engau
 */
public class JSONtreeTest {
    
    public JSONtreeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseObject method, of class JSONtree.
     */
//    @Test
    public void test_SingleStructure() {
        System.out.println("--------------");
        String json = "{\"proper1\":\"This is for test\",\"proper2\":12,\"proper3\":true";
        JSONtree target = new JSONtree();
        target.parse(json);
        System.out.println(target.getNicely());
        
    }
//    @Test
    public void test_ArrayStructure(){
        System.out.println("--------------");
        String json = "[\"This is for test\",12,true]";
        JSONtree target = new JSONtree();
        target.parse(json);
        System.out.println(target.getNicely());
    }
//    @Test
    public void test_ArrayJsonStructure(){
        System.out.println("--------------");
        String json = "[{\"proper1\":\"This is for test\"},{\"proper2\":12},{\"proper3\":true}]";
        JSONtree target = new JSONtree();
        
        target.put("hello", "world");
        target.parse(json);
        target.get(json);
        System.out.println(target.getNicely());
    }
    @Test
    public void test_GetKeysFromEmptyObject(){
        System.out.println("test_GetKeysFromEmptyObject--------------");
        JSONtree target = new JSONtree();
        target.put("first","primer");
        target.put("fsecond","segundo");
        target.put("fthird","tercer");
        System.out.println(""+target);
        ArrayList<String> keys = target.getKeys();
        
        String e = "";
        for(int i = 0; i < keys.size(); i++){
            e = keys.get(i);
            System.out.println(i+") "+e);
        }
    }
    @Test
    public void test_JSONtree_compare(){
        JSONtree target = new JSONtree();
        target.put("first","primer");
        target.put("fsecond","segundo");
        target.put("fthird","tercer");
        
        JSONtree target2 = new JSONtree();
        target2.put("first","primer");
        target2.put("fsecond","segundo");
        target2.put("fthird","tercer");
        
        if( !target.compareTo( target2 ) ){
            fail("The Json are differents when should be equals.");
        }
    }
    @Test
    public void test_JSONtree_compare2(){
        JSONtree target = new JSONtree();
        target.put("first","primerooo");
        target.put("fsecond","segundooooo");
        target.put("fthird","tercerooooo");
        
        JSONtree target2 = new JSONtree();
        target2.put("first","primer");
        target2.put("fsecond","segundo");
        target2.put("fthird","tercer");
        
        if( !target.compareTo( target2 ) ){
            fail("The Json are differents when should be equals.");
        }
    }
    @Test
    public void test_JSONtree_compareFull(){
        JSONtree target = new JSONtree();
        target.put("first","primer");
        target.put("fsecond","segundo");
        target.put("fthird","tercer");
        
        JSONtree target2 = new JSONtree();
        target2.put("first","primer");
        target2.put("fsecond","segundo");
        target2.put("fthird","tercer");
        
        if( !target.compareTo( target2 ) ){
            fail("The Json are differents when should be equals.");
        }
    }
    @Test
    public void test_JSONtree_compareFull2(){
        JSONtree target = new JSONtree();
        target.put("first","primerooo");
        target.put("fsecond","segundooooo");
        target.put("fthird","tercerooooo");
        
        JSONtree target2 = new JSONtree();
        target2.put("first","primer");
        target2.put("fsecond","segundo");
        target2.put("fthird","tercer");
        
        if( target.compareToFull( target2 ) ){
            System.out.println("------test_JSONtree_compareFull2");
            System.out.println("First Json: ");
            System.out.println( target.toString());
            System.out.println("");
            System.out.println("Second Json:");
            System.out.println(target2.toString());
            fail("The JSONtree are equals when should be different.");
        }
    }
    @Test
    public void test_JSONtree_getNonExistingKeys(){
        JSONtree target = new JSONtree();
        target.put("first","primerooo");
        target.put("second","segundooooo");
        target.put("third","tercerooooo");
        
        JSONtree target2 = new JSONtree();
        target2.put("first","primer");
        target2.put("second","segundo");
        target2.put("fourth","tercer");
        target2.put("third","tercer");
        
        ArrayList<String> keys = target2.getNonExistingKeys(target);
        if( keys.size() != 1){
            System.out.println("-------------test_JSONtree_getNonExistingKeys");
            if( keys.size() > 0 ){
                System.out.println("Non existing keys: ");
                for( String k: keys){
                    System.out.println( "   - " + k );
                }
            }
            fail( "The non existing keys shoudl be 1, but currently are [" + keys.size() + "] " );
        }
    }
}
