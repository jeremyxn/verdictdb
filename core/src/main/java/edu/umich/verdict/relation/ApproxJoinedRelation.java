package edu.umich.verdict.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.datatypes.Alias;
import edu.umich.verdict.datatypes.SampleParam;
import edu.umich.verdict.datatypes.SampleSizeInfo;
import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.condition.AndCond;
import edu.umich.verdict.relation.condition.CompCond;
import edu.umich.verdict.relation.condition.Cond;
import edu.umich.verdict.relation.expr.ColNameExpr;
import edu.umich.verdict.relation.expr.Expr;
import edu.umich.verdict.relation.expr.ExprModifier;
import edu.umich.verdict.relation.expr.FuncExpr;

public class ApproxJoinedRelation extends ApproxRelation {

	private ApproxRelation source1;
	
	private ApproxRelation source2;
	
	private List<Pair<Expr, Expr>> joinCols;
	
	/**
	 * 
	 * @param vc
	 * @param source1
	 * @param source2
	 * @param joinCols An empty joinCols indicates CROSS JOIN
	 */
	public ApproxJoinedRelation(VerdictContext vc, ApproxRelation source1, ApproxRelation source2, List<Pair<Expr, Expr>> joinCols) {
		super(vc);
		this.source1 = source1;
		this.source2 = source2;
		if (joinCols == null) {
			this.joinCols = new ArrayList<Pair<Expr, Expr>>();
		} else {
			this.joinCols = joinCols;
		}
	}
	
	public static ApproxJoinedRelation from(VerdictContext vc, ApproxRelation source1, ApproxRelation source2, List<Pair<Expr, Expr>> joinCols) {
		ApproxJoinedRelation r = new ApproxJoinedRelation(vc, source1, source2, joinCols);
		return r;
	}
	
	public static ApproxJoinedRelation from(VerdictContext vc, ApproxRelation source1, ApproxRelation source2, Cond cond) throws VerdictException {
		return from(vc, source1, source2, extractJoinConds(cond));
	}
	
	private static List<Pair<Expr, Expr>> extractJoinConds(Cond cond) throws VerdictException {
		if (cond == null) {
			return null;
		}
		if (cond instanceof CompCond) {
			CompCond cmp = (CompCond) cond;
			List<Pair<Expr, Expr>> l = new ArrayList<Pair<Expr, Expr>>();
			l.add(Pair.of(cmp.getLeft(), cmp.getRight()));
			return l;
		} else if (cond instanceof AndCond) {
			AndCond and = (AndCond) cond;
			List<Pair<Expr, Expr>> l = new ArrayList<Pair<Expr, Expr>>();
			l.addAll(extractJoinConds(and.getLeft()));
			l.addAll(extractJoinConds(and.getRight()));
			return l;
		} else {
			throw new VerdictException("Join condition must be an 'and' condition.");
		}
	}

	/*
	 * Approx
	 */
	
	@Override
	public ExactRelation rewrite() {
		Map<String, String> sub = tableSubstitution();
		List<Pair<Expr, Expr>> cols = new ArrayList<Pair<Expr, Expr>>();
		for (Pair<Expr, Expr> p : joinCols) {
			cols.add(Pair.of(exprWithTableNamesSubstituted(p.getLeft(), sub), exprWithTableNamesSubstituted(p.getRight(), sub)));
		}
		ExactRelation r = JoinedRelation.from(vc, source1.rewrite(), source2.rewrite(), cols);
		r.setAliasName(getAliasName());
		return r;
	}
	
	@Override
	protected double samplingProbabilityFor(FuncExpr f) {
		if (areMatchingUniverseSamples()) {
			return Math.min(source1.samplingProbabilityFor(f), source2.samplingProbabilityFor(f));
		} else {
			return source1.samplingProbabilityFor(f) * source2.samplingProbabilityFor(f);
		}
	}
	
	private boolean areMatchingUniverseSamples() {
		List<Expr> leftJoinCols = new ArrayList<Expr>();
		List<Expr> rightJoinCols = new ArrayList<Expr>();
		for (Pair<Expr, Expr> pair : joinCols) {
			leftJoinCols.add(pair.getLeft());
			rightJoinCols.add(pair.getRight());
		}
		
		return source1.sampleType().equals("universe") && source2.sampleType().equals("universe")
				&& joinColumnsEqualToSampleColumns(leftJoinCols, source1.sampleColumns())
				&& joinColumnsEqualToSampleColumns(rightJoinCols, source2.sampleColumns());
	}
	
	@Override
	public String sampleType() {
		Set<String> sampleTypeSet = ImmutableSet.of(source1.sampleType(), source2.sampleType());
		
		if (areMatchingUniverseSamples()) {
			return "universe";
		} else if (sampleTypeSet.equals(ImmutableSet.of("uniform", "uniform"))) {
			return "uniform";
		} else if (sampleTypeSet.equals(ImmutableSet.of("uniform", "stratified"))) {
			return "stratified";
		} else if (sampleTypeSet.equals(ImmutableSet.of("uniform", "universe"))) {
			return "uniform";
		} else if (sampleTypeSet.equals(ImmutableSet.of("uniform", "nosample"))) {
			return "uniform";
		} else if (sampleTypeSet.equals(ImmutableSet.of("stratified", "stratified"))) {
			return "stratified";
		} else if (sampleTypeSet.equals(ImmutableSet.of("stratified", "nosample"))) {
			return "stratified";
		} else if (sampleTypeSet.equals(ImmutableSet.of("universe", "nosample"))) {
			return "universe";
		} else {
			return null;
		}
	}
	
	@Override
	protected List<TableUniqueName> accumulateStratifiedSamples() {
		List<TableUniqueName> union = new ArrayList<TableUniqueName>(source1.accumulateStratifiedSamples());
		union.addAll(source2.accumulateStratifiedSamples());
		return union;
	}
	
	@Override
	public List<String> sampleColumns() {
		if (sampleType().equals("stratified")) {
			List<String> union = new ArrayList<String>(source1.sampleColumns());
			union.addAll(source2.sampleColumns());
			return union;
		} else if (sampleType().equals("universe")) {
			return source1.sampleColumns();
		} else {
			return Arrays.asList();
		}
	}
	
	private boolean joinColumnsEqualToSampleColumns(List<Expr> joinCols, List<String> sampleColNames) {
		List<String> joinColNames = new ArrayList<String>();
		for (Expr expr : joinCols) {
			if (expr instanceof ColNameExpr) {
				joinColNames.add(((ColNameExpr) expr).getCol());
			}
		}
		return joinColNames.equals(sampleColNames);
	}
	
	@Override
	protected Map<String, String> tableSubstitution() {
		return ImmutableMap.<String,String>builder().putAll(source1.tableSubstitution()).putAll(source2.tableSubstitution()).build();
	}

}
