package com.simperium.storage;

import com.simperium.android.StorageProvider;
import com.simperium.android.Bucket;

import com.simperium.client.BucketSchema;
import com.simperium.client.BucketSchema.Index;
import com.simperium.client.Query;
import com.simperium.client.Syncable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Naive implementation of a StorageProvider in memory.
 */
public class MemoryStore implements StorageProvider {
    
    public <T extends Syncable> BucketStore<T> createStore(String bucketName, BucketSchema<T> schema){
        return new Storage<T>();
    }
    
    class Storage<T extends Syncable> implements StorageProvider.BucketStore<T> {
        private Map<String, T> objects = Collections.synchronizedMap(new HashMap<String, T>(32));

        @Override
        public void prepare(Bucket<T> bucket){
            // noop
        }

        /**
         * Add/Update the given object
         */
        @Override
        public void save(T object, List<Index> indexes){
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
        public Bucket.ObjectCursor<T> all(){
            return null;
        }

        /**
         * Search
         */
        public Bucket.ObjectCursor<T> search(Query query){
            return null;
        }

        /**
         * Count
         */
        public int count(Query query){
            return 0;
        }
    }

}