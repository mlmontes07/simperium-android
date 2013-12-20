package com.simperium;

import com.simperium.client.Bucket;
import com.simperium.client.BucketNameInvalid;
import com.simperium.client.BucketObjectNameInvalid;
import com.simperium.client.BucketSchema;
import com.simperium.client.GhostStorageProvider;
import com.simperium.client.User;
import com.simperium.models.Note;
import com.simperium.storage.MemoryStore;
import com.simperium.test.MockCache;
import com.simperium.test.MockChannel;
import com.simperium.test.MockGhostStore;
import com.simperium.test.MockExecutor;

import static com.simperium.TestHelpers.makeUser;

public class BucketTest extends BaseSimperiumTest {

    public static final String TAG="SimperiumTest";
    private Bucket<Note> mBucket;
    private BucketSchema<Note> mSchema;
    private User mUser;
    private GhostStorageProvider mGhostStore;
    private Bucket.Channel mChannel;

    private static String BUCKET_NAME="local-notes";

    protected void setUp() throws Exception {
        super.setUp();

        mUser = makeUser();

        mSchema = new Note.Schema();
        MemoryStore storage = new MemoryStore();
        mGhostStore = new MockGhostStore();
        MockCache<Note> cache = new MockCache<Note>();
        mBucket = new Bucket<Note>(MockExecutor.immediate(), BUCKET_NAME, mSchema, mUser, storage.createStore(BUCKET_NAME, mSchema), mGhostStore, cache);
        mChannel = new MockChannel(mBucket);
        mBucket.setChannel(mChannel);
        mBucket.start();
    }

    public void testBucketName()
    throws Exception {
        assertEquals(BUCKET_NAME, mBucket.getName());
        assertEquals(mSchema.getRemoteName(), mBucket.getRemoteName());
    }

    public void testBuildObject()
    throws Exception {
        Note note = mBucket.newObject();
        assertTrue(note.isNew());
    }

    public void testSaveObject()
    throws Exception {
        Note note = mBucket.newObject();
        note.setTitle("Hello World");
        assertTrue(note.isNew());
        note.save();

        assertFalse(note.isNew());
    }

    public void testInvalidObjectNameThrowsException()
    throws Exception {
        BucketObjectNameInvalid exception = null;
        try {
            mBucket.newObject("bad name");
        } catch (BucketObjectNameInvalid e) {
            exception = e;
        }

        assertNotNull(exception);
    }

    public void testTrimObjectNameWhiteSpace()
    throws Exception {
        BucketObjectNameInvalid exception = null;
        Note note = mBucket.newObject("  whitespace ");

        assertEquals("whitespace", note.getSimperiumKey());

    }

    public void testValidateBucketName()
    throws Exception {
        BucketNameInvalid exception = null;
        try {
            Bucket.validateBucketName("hello world");
        } catch (BucketNameInvalid e) {
            exception = e;
        }

        assertNotNull(exception);
    }

}