// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.commons.utils.data.text.StringHelper;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.perl.PerlLanguage;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DataMapExpressionParser {

    // private String expression;
    private Perl5Matcher matcher = new Perl5Matcher();

    private Perl5Compiler compiler = new Perl5Compiler();

    private Set<TableEntryLocation> resultList = new HashSet<TableEntryLocation>();

    private Pattern pattern;

    private PatternMatcherInput patternMatcherInput;

    private String locationPattern;

    private ILanguage language;

    public DataMapExpressionParser(ILanguage language) {
        super();
        this.language = language;
        setLocationPattern(language.getLocationPattern());
    }

    /**
     * DOC amaumont Comment method "setRegexpPattern".
     * 
     * @param regexpPattern
     */
    public void setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
    }

    public TableEntryLocation[] parseTableEntryLocations(String expression) {
        resultList.clear();
        if (expression != null) {
            matcher.setMultiline(true);
            if (patternMatcherInput == null) {
                patternMatcherInput = new PatternMatcherInput(expression);
            } else {
                patternMatcherInput.setInput(expression);
            }

            recompilePatternIfNecessary(locationPattern);

            while (matcher.contains(patternMatcherInput, pattern)) {
                MatchResult matchResult = matcher.getMatch();
                resultList.add(new TableEntryLocation(matchResult.group(1), matchResult.group(2)));
            }
        }
        return resultList.toArray(new TableEntryLocation[0]);
    }

    private void recompilePatternIfNecessary(String regexpPattern) {
        if (pattern == null || !regexpPattern.equals(pattern.getPattern())) {
            try {
                pattern = compiler.compile(regexpPattern);
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String addTablePrefixToColumnName(String uniqueNameComponent, String expression,
            TableEntryLocation[] locations, boolean prefixTableNameWithComponentName,
            HashSet<TableEntryLocation> validColumnEntryLocations) {
        String returnedExpression = expression;
        for (TableEntryLocation location : locations) {
            recompilePatternIfNecessary(StringHelper.replacePrms(language.getSubstPatternForPrefixColumnName(),
                    new Object[] { Perl5Compiler.quotemeta(location.tableName), Perl5Compiler.quotemeta(location.columnName) }));
            if (returnedExpression != null) {
                matcher.setMultiline(true);
                Perl5Substitution substitution = new Perl5Substitution(language.getPrefixTableRegexp()
                        + (prefixTableNameWithComponentName ? uniqueNameComponent + "__" : "") + "$1->" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + language.getPrefixFieldRegexp()
                        + (validColumnEntryLocations.contains(location) ? uniqueNameComponent + "__$1__" : "") //$NON-NLS-1$ //$NON-NLS-2$
                        + "$2" + language.getSuffixFieldRegexp(), Perl5Substitution.INTERPOLATE_ALL); //$NON-NLS-1$
                returnedExpression = Util.substitute(matcher, pattern, substitution, returnedExpression,
                        Util.SUBSTITUTE_ALL);
            }
        }
        return returnedExpression;
    }

    public String addRefArrayPointer(String expression, TableEntryLocation[] locations) {
        String returnedExpression = expression;
        PerlLanguage perlLanguage = (PerlLanguage) language;
        for (TableEntryLocation location : locations) {
            recompilePatternIfNecessary(StringHelper.replacePrms(perlLanguage.getSubstPatternToAddRefArrayPointer(),
                    new Object[] { location.tableName }));
            if (returnedExpression != null) {
                matcher.setMultiline(true);
                Perl5Substitution substitution = new Perl5Substitution(
                        language.getPrefixTableRegexp() + "$1->" //$NON-NLS-1$
                                + perlLanguage.getPrefixFieldRegexp() + "$2" + perlLanguage.getSuffixFieldRegexp(), Perl5Substitution.INTERPOLATE_ALL); //$NON-NLS-1$
                returnedExpression = Util.substitute(matcher, pattern, substitution, returnedExpression,
                        Util.SUBSTITUTE_ALL);
            }
        }
        return returnedExpression;
    }

    public String replaceLocation(String expression, TableEntryLocation oldLocation, TableEntryLocation newLocation) {
        String returnedExpression = expression;
        String tempPattern = StringHelper.replacePrms(language.getSubstPatternForReplaceLocation(), new Object[] {
                oldLocation.tableName, oldLocation.columnName });
        recompilePatternIfNecessary(tempPattern);
        if (returnedExpression != null) {
            matcher.setMultiline(true);
            Perl5Substitution substitution = new Perl5Substitution(language.getPrefixTableRegexp()
                    + "$1" + newLocation.tableName + "$2" //$NON-NLS-1$ //$NON-NLS-2$
                    + language.getSuffixTableRegexp()
                    + "$3" + language.getPrefixFieldRegexp() + "$4" + newLocation.columnName + "$5" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + language.getSuffixFieldRegexp(), Perl5Substitution.INTERPOLATE_ALL);
            returnedExpression = Util.substitute(matcher, pattern, substitution, returnedExpression,
                    Util.SUBSTITUTE_ALL);
        }
        return returnedExpression;
    }

}
