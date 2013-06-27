package com.simperium.storage;

import com.simperium.client.Bucket;
import com.simperium.client.BucketSchema;
import com.simperium.client.Syncable;
import com.simperium.storage.StorageProvider.BucketStore;
import com.simperium.storage.StorageProvider.BucketCursor;

import com.simperium.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;

/**
 * Naive implementation of a StorageProvider in memory.
 */
public class MemoryStore implements StorageProvider {
    
    public <T extends Syncable> BucketStore<T> createStore(BucketSchema<T> schema){
        return new Storage<T>(schema);
    }
    
    class Storage<T extends Syncable> extends StorageProvider.BucketStore<T> {
        public Storage(BucketSchema<T> schema){
            super(schema);
        }
        private Map<String, T> objects = Collections.synchronizedMap(new HashMap<String, T>(32));
        /**
         * Add/Update the given object
         */
        @Override
        public void save(T object){
            objects.put(object.getSimperiumKey(), object);
        }
        /**
         * Remove the given object from the storage
         */
        @Override
        public void delete(T object){
            objects.remove(object.getSimperiumKey());
        }
        /**
         * Delete all objects from storage
         */
        @Override
        public void reset(){
            objects.clear();
        }
        /**
         * Get an object with the given key
         */
        @Override
        public T get(String key){
            return objects.get(key);
        }
        /**
         * Get a cursor to all the objects
         */
        public BucketCursor<T> all(){
            return null;
        }

    }

}