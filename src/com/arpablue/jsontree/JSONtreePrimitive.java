/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.jsontree;

import com.arpablue.jsontree.JSONtree;
import com.arpablue.jsontree.TreeLeaf;
import com.arpablue.jsontree.JSONtreeBase;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Augusto Flores
 */
public class JSONtreePrimitive extends JSONtreeBase{
    protected static int DEEPER = 0;
    /**
     * This contain the reference for the children of the current node.
     */
    protected ArrayList<JSONtreeBase> mChildren;
    protected String beginingObj(){
        if( this.isArray()){
            return "[";
        }
        return "{";
    }
    protected String endingObj(){
        if( this.isArray()){
            return "]";
        }
        return "}";
    }
    @Override
    public String toString() {
        if (this.mChildren == null) {
            return ("null");
        }
        if (this.mChildren.size() < 1) {
            return (beginingObj()+endingObj());
        }
        String res = null;
        if( this.getKey() == null){
            res = beginingObj();
        }else{
            res = "\""+this.getKey()+"\":"+beginingObj();
        }
        boolean first = true;
        for (JSONtreeBase e : this.mChildren) {
            if (first) {
                first = false;
                //res = res + e.toString();
                if( !this.isArray()){
                    res = res + "\"" + e.getKey() + "\":";
                }
                res = res + e.get();
            } else {
                //res = res + "," + e.toString();
                res = res + ",";
                if( !this.isArray()){
                    res = res + "\"" + e.getKey() + "\":";
                }
                res = res + e.get();
            }
        }
        res = res + endingObj();
        return (res);    
    }
    /**
     * It return the current object.
     * @return It is the current object.
     */
    @Override
    public Object get(){
        return this.getChildren();
    }
    /**
     * 
     */
    public JSONtreePrimitive(){
        super();
        mChildren = new ArrayList<JSONtreeBase>();
    }
    /**
     * It return the number of children that exists in the current node.
     * @return 
     */
    public int size(){
        if(mChildren == null){
            return -1;
        }
        return mChildren.size();
    }
    /**
     * It return the key of a child specified by index.
     * @param index It is the child that is necessary get the key.
     * @return It is the corresponding key to  the index child.
     */
    public String getKey(int index){
        if(mChildren == null){
            return null;
        }
        if(mChildren.size() < 1){
            return null;
        }
        
        JSONtreeBase e = mChildren.get(index);
        return e.getKey();
    }
    /**
     * It return the value of a child specified by index.
     * @param index It is the child that is necessary get the value.
     * @return It is the value of the corresponding index child.
     */
    public Object get(int index){
        if(mChildren == null){
            return null;
        }
        if(mChildren.size() < 1){
            return null;
        }
        
        JSONtreeBase e = mChildren.get(index);
        return e.get();
    }
    /**
     * It return an ArrayList with all keys to access to all elements.
     * @return It is the an array list with all current keys.
     */
    public ArrayList<String> getKeys(){
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> e = new ArrayList<String>();
        String key = "";
        if(this.getKey() != null){
            key = this.getKey();
        }
        for( JSONtreeBase node : this.mChildren){
            if(!node.isNode()){
                res.add(key+"/"+node.getKey());
            }else{
                e = ((JSONtree)node).getKeys();
                for(String k: e){
                    res.add(key+"/"+k);
                }
            }
        }
        return res;
    }
    /**
     * It return the ArrauyList of the current node.
     * @return it is an ArrayList object with all children.
    */
    public ArrayList<JSONtreeBase> getChildren(){
        return this.mChildren;
    }
    /**
     * It return a value from a specific path. The method return null if 
     * the element not exist or the value is null, use the exist() to verify that 
     * the path exists.
     */
    public Object get(String path){
        if( path == null){
            return null;
        }
        path = path.trim();
        path = path.replace("\\", "/");
        path = path.replace(" ", "0oo0");
        path = path.replace("/", " ");
        path = path.trim();
        path = path.replace(" ", "/");
        path = path.replace("0oo0"," ");
        String[]v = path.split("/");
        return JSONtreePrimitive.this.get(v,0);

    }
    /**
     * It return a value according a path of keys specified in an array.
     * @param v It is the path of keys for each level.
     * @param pos It is the current position of level of the key.
     * @return It is the value of the key, if the key not exists then return null.
     */
    Object get(String[] v, int pos){
        if(v == null){
            return null;
        }
        if(pos < 0){
            pos = 0;
        }
        if(v.length <= pos ){
            return null;
        }
        JSONtreeBase child = this.getChild(v[pos]);
        
        if(child == null){
            return null;
        }
        if( pos == v.length - 1){
            return child.get();
        }
        if( child.isNode()){
            return ((JSONtree)child).get(v,pos+1);
        }
        return getReturnValue(child.get(),v,pos+1);
    }
    /**
     * It return a value respective to the current node.
     * @param value It is the current node.
     * @param v It is the arrays of keys.
     * @param pos It is the position of the current key.
     * @return 
     */
    Object getReturnValue(Object value, String[] v, int pos){
        if( value == null){
            return value;
        }
        if(pos >= v.length){
            return null;
        }
        if( value instanceof JSONtree){
            return ((JSONtree)value).get( v, pos);
        }
        if( value instanceof TreeLeaf){
            return getReturnValue(((TreeLeaf)value).get(),v,pos );
        }
        if( JSONtreeBase.isArray(value) ){
            return getArrayValue( value,v,pos );
        }
        if( JSONtreeBase.isCollection(value) ){
            return getListValue( value,v,pos );
        }
        return null;
    }
    /**
     * It return the list of values, it is specified by a key path.
     * @param target It is the current node.
     * @param v It is the array of keys.
     * @param pos It is the current level in the array key.
     * @return It is the value according to the key path.
     */
    Object getListValue(Object target,String[] v, int pos ){
        if (target == null){
            return null;
        }
        if(  v.length <= pos){
            return null;
        }
        ArrayList list = (ArrayList)target;
        int i;
        i = -1;
        try{
            i = Integer.parseInt(v[pos]);
        }catch( Exception e){
            i = -1;
        }
        if( i < 0){
            return null;
        }
        if( list.size() <= i){
            return null;
        }
        Object elem = list.get(i);
        if(pos == v.length - 1){
            return elem;
        }
        
        if( elem instanceof JSONtree){
            return ((JSONtree)elem).get( v, pos);
        }
        if( elem instanceof TreeLeaf){
            return getReturnValue(((TreeLeaf)elem).get(),v,pos );
        }
        return getReturnValue(elem,v,pos + 1);
    }
    /**
     * It return the value corresponding to an array.
     */
    Object getArrayValue(Object target, String[] v, int pos){
        
        String res;
        if (target == null) {
            return "null";
        }
        int arrlength = Array.getLength(target);
        if (arrlength < 1) {
            return null;
        }
        int i;
        try{
            i = Integer.parseInt(v[pos]);
        }catch(Exception e){
            i = -1;
        }
        if(i < 0){
            return null;
        }
        if(i >= arrlength){
            return null;
        }
        Object e;
        e = Array.get(target, i);
        if( pos == arrlength-1){
            return e;
        }
        
        return getReturnValue(e,v,pos+1);
    }
    /**
     * It return true if the path exists.
     */
    public boolean exist(String path){
        if( path == null){
            return false;
        }
        path = path.trim();
        path = path.replace("\\", "/");
        path = path.replace(" ", "0oo0");
        path = path.replace("/", " ");
        path = path.trim();
        path = path.replace(" ", "/");
        path = path.replace("0oo0"," ");
        String[]v = JSONtree.splitFirstOcurrence(path,"/");
        JSONtreeBase child = this.getChild(v[0]);
        if(child == null){
            return false;
        }
        if(v == null){
            return false;
        }
        if(v.length == 1){
            return true;
        }
        if(!child.isNode()){
            return false;
        }
        return ((JSONtree)child).exist(v[1]);
    }
    /**
     * Introduce a value in a respective path.
     */
    public boolean put(String path, Object value){
        return this.add(path, value);
    }
    /**
     * Introduce a object in a specific path.
     */
    public boolean add(String path, Object value) {
        if (path == null) {
            return false;
        }
        if (path.length() < 1) {
            return false;
        }
        path = path.replace("\\", "/");
        path = path.replace(" ", "-_-");
        path = path.replace("/", " ");
        path = path.trim();
        path = path.replace(" ", "/");
        path = path.replace("-_-", " ");
        String[] v = path.split("/");
        // return addValue(path, 0,value);
        return addValue(v, 0,value);
    }
    /**
     * It add a new value to the current node, if the path specify another
     * level, then create the and node and add the value to the new node.
     */
    boolean addValue(String[] paths, int pos, Object value) {
        if( paths == null){
            return false;
        }
        if(paths.length-1 < pos){
            return false;
        }
        if(paths.length-1 == pos){
            return setChild( paths[ pos ], value);
        }
        if(pos >= paths.length){
            return false;
        }
        String key = paths[pos];
        ///recursividad
        JSONtreeBase child = this.getChild( key );
        
        if( child == null){
            child = new JSONtree();
            child.setKey( key );
            this.mChildren.add( child );
        }
        if(!child.isNode()){
            JSONtreeBase childOld = child;
            child = new JSONtree();
            child.setKey( key );
            this.replace( childOld,child );
        }
        return ((JSONtree)child).addValue( paths,pos+1, value );
    }
    /**
     * It replace a node by another
     */
    protected boolean replace(JSONtreeBase oldNode, JSONtreeBase newNode){
        if(oldNode == null){
            return false;
        }
        int index = this.getChildPosition(oldNode.getKey());
        if(newNode == null){
            return false;
        }
        if( index < 0 ){
            this.mChildren.add( newNode);
        }
        this.mChildren.set(index, newNode);
        return true;
    }
    /**
     * It set a child in the current node.
     */
    public boolean setChild(String key, Object value){
       if( key == null){
           return false;
       }
       int pos = this.getChildPosition(key);
       if( pos < 0){
            TreeLeaf leaft = new TreeLeaf();
            leaft.setKey(key);
            leaft.setValue(value);
            this.mChildren.add(leaft);
            return true;
       }
       JSONtreeBase child = this.getChild(pos);
       if(!child.isNode()){
           ((TreeLeaf)child).setValue( value );
           return true;
           
       }
       child = new TreeLeaf(key, value);
       this.mChildren.set(pos, child);
       return true;
    }
    /**
     * It extract the data from an object that has an array
     * @param target It is the object to extract the data.
     * @return It is true if the data has been extracted and added to the current structure.
     */
    public boolean parseObject(Object target){
        if( JSONtreeBase.isArray(target)){
            return convertFromArray( target );
        }
        return false;
    }
    /**
     * It extract the elements from an array object,  this elements are add to the current TrePath.
     * @param target It is the array object.
     * @return It is true if the elements has been added without problems.
     */
    public boolean convertFromArray(Object target){
        String res;
        this.setIsArray( true );
        if (target == null) {
            return false;
        }
        int arrlength = Array.getLength(target);
        if (arrlength < 1) {
            return false;
        }
        Object e;
        TreeLeaf elem;
        for (int i = 0; i < arrlength; i++) {
            e = Array.get(target, i);
            elem = new TreeLeaf(i+"",e);
        }
        return true;
    }
    /**
     * It split a string in two substring 
     * @param target
     * @param separator
     * @return 
     */
    public static String[] splitFirstOcurrence(String target, String separator) {
        String[] res = new String[2];
        res[0] = null;
        res[1] = null;
        if (target == null) {
            return res;
        }
        if (separator == null) {
            res = new String[1];
            res[0] = target;
        }
        String[] v = target.split(separator);
        if(v.length <= 1){
            return v;
        }
            
        res[0] = v[0];
        res[1] = target.replace( v[0]+separator,"");
        return res;
    }
    /**
     * It replace the node by a TreeLeaf object .
     */
    protected boolean replaceNode(String key, Object value) {
        int pos = this.getChildPosition(key);
        if (pos < 0) {
            return false;
        }
        TreeLeaf s = new TreeLeaf(key, value);
        this.mChildren.remove(pos);
        this.mChildren.add(pos, s);
        return true;
    }
    /**
     * It return the position of the node that belong a respective key. If the
     * node not exists or no keys has a match then return -1.
     */
    protected int getChildPosition(String key) {
        if (key == null) {
            return -1;
        }
        if (this.mChildren == null) {
            return -1;
        }
        if (this.mChildren.size() < 1) {
            return -1;
        }
        int pos = 0;
        for (JSONtreeBase e : this.mChildren) {
            if (e.equalKey(key)) {
                return pos;
            }
            pos++;
        }
        return -1;
    }
    /**
     * It return the node of the specified index.
     */
    protected JSONtreeBase getChild(int pos) {
        if (pos < 0) {
            pos = pos * -1;
        }
        if (this.mChildren == null) {
            return null;
        }
        if (pos >= this.mChildren.size()) {
            return null;
        }
        return this.mChildren.get(pos);
    }
    /**
     * It add a new brunch to the current node.
     */
    protected boolean addNewChild(String key, Object value) {
        TreeLeaf n = new TreeLeaf();
        if (key == null) {
            return false;
        }
        if (key.length() < 1) {
            return false;
        }
        n.setKey(key);
        n.setValue(value);
        if (this.mChildren == null) {
            this.mChildren = new ArrayList<JSONtreeBase>();
        }
        this.mChildren.add(n);
        return true;
    }
    /**
     * It add a new node to the current node.
     */
    protected boolean addNewChild(JSONtree e) {
        if (e == null) {
            return false;
        }
        if (this.mChildren == null) {
            this.mChildren = new ArrayList<JSONtreeBase>();
        }
        this.mChildren.add(e);
        return true;
    }
    /**
     * It return the node that have the same key, if no found a node with the
     * same key then return null.
     */
    protected JSONtreeBase getChild(String key) {
        if (this.mChildren == null) {
            return null;
        }
        if (this.mChildren.size() < 1) {
            return null;
        }

        for (JSONtreeBase e : this.mChildren) {
            if (e.equalKey(key)) {
                return e;
            }
        }
        return null;
    }
    /**
     * Compare the to JSONtree object and return true if booth has the same keys.
     */
    public boolean compareTo(JSONtree target){
        if( target == null){
            return false;
        }
        ArrayList<String> myKeys = this.getKeys();
        ArrayList<String> itsKeys = target.getKeys();
        boolean flag = false;
        for (int i = 0; i < myKeys.size(); i++) {
            flag = false;
            String  me = myKeys.get(i);
            for( int j = 0; j < itsKeys.size(); j++){
                String it = itsKeys.get(j);
                if(it.equalsIgnoreCase(me)){
                    flag = true;
                    break;
                }
            }
            if( !flag ){
                return false;
            }
                
        }
        for (int i = 0; i < itsKeys.size(); i++) {
            flag = false;
            String  me = itsKeys.get(i);
            for( int j = 0; j < myKeys.size(); j++){
                String it = myKeys.get(j);
                if(it.equalsIgnoreCase(me)){
                    flag = true;
                    break;
                }
            }
            if( !flag ){
                return false;
            }
                
        }
        return true;
    }
    /**
     * It compare the structure and the values of the attributes.
     * @param target It is the another JSONtree object to be compare.
     * @return It is true if both JSONtree are equals.
     */
    public boolean compareToFull( JSONtree target){
        if(! this.compareTo(target)){
            return false;
        }
        ArrayList<String> keys = this.getKeys();
        Object v1 = null;
        Object v2 = null;
        for( String key : keys){
            v1 = this.get(key);
            v2 = target.get(key);
            if( !compareObjects(v1, v2) ){
                return false;
            }
        }
        return true;
    }
    /**
     * It compare two Objects.
     * @param obj1 The first object to compare.
     * @param obj2 The second object to compare.
     * @return It is true if both objects are compare.
     */
    protected static boolean compareObjects(Object obj1, Object obj2){
        if( ( obj1 == null ) && ( obj2 == null )){
            return true;
        }
        if( obj1 == null ){
            return false;
        }
        if( obj2 == null ){
            return false;
        }
        return obj1.toString().equalsIgnoreCase( obj2.toString() );
    }
    /**
     * It remove all elements of the tree.
     * @return 
     */
    public boolean clear(){
        if(this.mChildren == null){
            return true;
        }
        for( JSONtreeBase e: mChildren){
            e.clear();
        }
        this.mChildren.clear();
        return true;
    }
    /**
     * It return the keys that exist in the current object, but not exists in the target JSONtree.
     * @param target It is the JSONtree to compare the keys.
     * @return It is the list of keys that not exist in the JSONtree target object.
     */
    public ArrayList<String> getNonExistingKeys(JSONtree target){
        ArrayList<String> res  = new ArrayList<String>();
        if( target == null ){
            return res;
        }
        ArrayList<String> keys1 = this.getKeys();
        ArrayList<String> keys2 = target.getKeys();
        boolean flag = false;
        for( String k1 : keys1){
            flag = false;
            for( String k2: keys2 ){
                if( k1.equalsIgnoreCase( k2 )){
                    flag = true;
                    break;
                }
            }
            if( !flag){
                res.add( k1 );
            }
        }
        
        return res;
    }

    
}
