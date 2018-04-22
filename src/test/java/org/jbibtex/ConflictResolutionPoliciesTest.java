package org.jbibtex;

import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicies;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class ConflictResolutionPoliciesTest {
    @Test
    public void parsingWithIgnoreSubsequentPolicyYieldsFirstEntry() throws ParseException, UnsupportedEncodingException {
        BibTeXParser parser = new BibTeXParser(BibTeXEntryKeyConflictResolutionPolicies.IGNORE_SUBSEQUENT);
        BibTeXDatabase db = parser.parse(
                new InputStreamReader(getClass().getResourceAsStream("/duplicateKey.bib"), "US-ASCII")
        );

        assertEquals(1, db.getEntries().size());
        assertEquals(
                "EnTagRec++: An enhanced tag recommendation system for software information sites",
                db.getEntries().get(new Key("Wang20171")).getField(new Key("title")).toUserString()
        );
    }

    @Test
    public void parsingWithDefaultPolicyBehavesAsIgnoreSubsequent() throws ParseException, UnsupportedEncodingException {
        BibTeXParser parser = new BibTeXParser();
        BibTeXDatabase db = parser.parse(
                new InputStreamReader(getClass().getResourceAsStream("/duplicateKey.bib"), "US-ASCII")
        );

        assertEquals(1, db.getEntries().size());
        assertEquals(
                "EnTagRec++: An enhanced tag recommendation system for software information sites",
                db.getEntries().get(new Key("Wang20171")).getField(new Key("title")).toUserString()
        );
    }

    @Test
    public void parsingWithOverrideWithSubsequentPolicyYieldsLastEntry() throws ParseException, UnsupportedEncodingException {
        BibTeXParser parser = new BibTeXParser(BibTeXEntryKeyConflictResolutionPolicies.OVERRIDE_WITH_SUBSEQUENT);
        BibTeXDatabase db = parser.parse(
                new InputStreamReader(getClass().getResourceAsStream("/duplicateKey.bib"), "US-ASCII")
        );

        assertEquals(1, db.getEntries().size());
        assertEquals(
                "Understanding the factors for fast answers in technical Q&A websites: An empirical study of four stack exchange websites",
                db.getEntries().get(new Key("Wang20171")).getField(new Key("title")).toUserString()
        );
    }

    @Test(expected = DuplicateKeyException.class)
    public void parsingWithThrowOnDuplicateThrowsWhenDuplicateIsEncountered() throws ParseException, UnsupportedEncodingException {
        BibTeXParser parser = new BibTeXParser(BibTeXEntryKeyConflictResolutionPolicies.THROW_ON_DUPLICATE);
        BibTeXDatabase db = parser.parse(
                new InputStreamReader(getClass().getResourceAsStream("/duplicateKey.bib"), "US-ASCII")
        );
    }

    @Test
    public void parsingWithThrowOnDuplicateParsesValidFileWithoutIssues() throws ParseException, UnsupportedEncodingException {
        BibTeXParser parser = new BibTeXParser(BibTeXEntryKeyConflictResolutionPolicies.THROW_ON_DUPLICATE);
        BibTeXDatabase db = parser.parse(
                new InputStreamReader(getClass().getResourceAsStream("/zotero.bib"), "US-ASCII")
        );
        assertEquals(1, db.getEntries().size());
    }

    @Test
    public void parsingWithRekeySubsequentPolicyYieldsBothEntries() throws ParseException, UnsupportedEncodingException {
        BibTeXParser parser = new BibTeXParser(BibTeXEntryKeyConflictResolutionPolicies.REKEY_SUBSEQUENT);
        BibTeXDatabase db = parser.parse(
                new InputStreamReader(getClass().getResourceAsStream("/duplicateKey.bib"), "US-ASCII")
        );

        assertEquals(2, db.getEntries().size());
        assertEquals(
                "EnTagRec++: An enhanced tag recommendation system for software information sites",
                db.getEntries().get(new Key("Wang20171")).getField(new Key("title")).toUserString()
        );
        assertEquals(
                "Understanding the factors for fast answers in technical Q&A websites: An empirical study of four stack exchange websites",
                db.getEntries().get(new Key("Wang20171-1")).getField(new Key("title")).toUserString()
        );
    }
}
