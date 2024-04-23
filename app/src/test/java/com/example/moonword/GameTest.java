package com.example.moonword;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameTest {
    @Test
    public void esParaulaSolucioTest() {
        assertTrue(Game.esParaulaSolucio("puresa", "apres"));
        assertFalse(Game.esParaulaSolucio("puresa", "apresa"));
        assertTrue(Game.esParaulaSolucio("copta", "coa"));
        assertTrue(Game.esParaulaSolucio("saca", "casa"));
        assertTrue(Game.esParaulaSolucio("feiner", "ene")) ;
        assertTrue(Game.esParaulaSolucio("nado", "dona"));
        assertFalse(Game.esParaulaSolucio("nimfa", "fama"));
    }
}