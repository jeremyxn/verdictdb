package org.verdictdb.core.sqlobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class ColumnOp implements UnnamedColumn, SelectItem, Serializable {

  private static final long serialVersionUID = 4600444500423496880L;

  /**
   * opType must be one of the following.
   * <p>
   * Functions:
   * <ol>
   * <li>sum</li>
   * <li>count</li>
   * <li>avg</li>
   * <li>add</li>
   * <li>multiply</li>
   * <li>subtract</li>
   * <li>divide</li>
   * <li>stddev_pop</li>
   * <li>stddev_samp</li>
   * <li>pow</li>
   * <li>sqrt</li>
   * <li>min</li>
   * <li>max</li>
   * <li>countdistinct</li>
   * <li>substring</li>
   * <li>rand</li>
   * <li>floor</li>
   * <li>cast</li>
   * <li>percentile</li>
   * </ol>
   * <p>
   * Comparison:
   * <ol>
   * <li>and</li>
   * <li>or</li>
   * <li>equal</li>
   * <li>notequal</li>
   * <li>notgreaterthan</li>
   * <li>notlessthan</li>
   * <li>casewhenelse</li>
   * <li>whenthenelse</li>
   * <li>is null</li>
   * <li>is not null</li>
   * <li>interval</li>
   * <li>date</li>
   * <li>greater</li>
   * <li>less</li>
   * <li>greaterequal</li>
   * <li>lessequal</li>
   * <li>like</li>
   * <li>notlike</li>
   * <li>exists</li>
   * <li>notexists</li>
   * <li>between</li>
   * <li>in</li>
   * <li>notin</li>
   * <li>year</li>
   * </ol>
   */
  String opType;

  List<UnnamedColumn> operands = new ArrayList<>();

  public ColumnOp(String opType) {
    this.opType = opType;
  }

  public ColumnOp(String opType, UnnamedColumn operand) {
    this.operands = Arrays.asList(operand);
    this.opType = opType;
  }

  public ColumnOp(String opType, List<UnnamedColumn> operands) {
    this.operands = operands;
    this.opType = opType;
  }

  public UnnamedColumn getOperand() {
    return getOperand(0);
  }

  public UnnamedColumn getOperand(int i) {
    return operands.get(i);
  }

  public List<UnnamedColumn> getOperands() {
    return operands;
  }

  public void setOperand(List<UnnamedColumn> operands) {
    this.operands = operands;
  }

  public void setOperand(Integer index, UnnamedColumn operand) {
    this.operands.set(index, operand);
  }

  public String getOpType() {
    return opType;
  }

  public void setOpType(String opType) {
    this.opType = opType;
  }

  public static ColumnOp and(UnnamedColumn predicate1, UnnamedColumn predicate2) {
    return new ColumnOp("and", Arrays.asList(predicate1, predicate2));
  }
  
  public static ColumnOp or(UnnamedColumn predicate1, UnnamedColumn predicate2) {
    return new ColumnOp("or", Arrays.asList(predicate1, predicate2));
  }

  public static ColumnOp count() {
    return new ColumnOp("count");
  }

  public static ColumnOp equal(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("equal", Arrays.asList(column1, column2));
  }

  public static ColumnOp notequal(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("notequal", Arrays.asList(column1, column2));
  }


  public static ColumnOp notgreaterthan(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("notgreaterthan", Arrays.asList(column1, column2));
  }

  public static ColumnOp notlessthan(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("notlessthan", Arrays.asList(column1, column2));
  }

  public static ColumnOp add(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("add", Arrays.asList(column1, column2));
  }
  
  public static ColumnOp subtract(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("subtract", Arrays.asList(column1, column2));
  }

  public static ColumnOp multiply(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("multiply", Arrays.asList(column1, column2));
  }

  public static ColumnOp divide(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("divide", Arrays.asList(column1, column2));
  }

//  public static ColumnOp casewhenelse(UnnamedColumn columnIf, UnnamedColumn condition, UnnamedColumn columnElse) {
//    return new ColumnOp("casewhenelse", Arrays.asList(columnIf, condition, columnElse));
//  }

//  public static ColumnOp notnull(UnnamedColumn column1) {
//    return new ColumnOp("notnull", Arrays.asList(column1));
//  }

  public static ColumnOp std(UnnamedColumn column1) {
    return new ColumnOp("stddev_pop", Arrays.asList(column1));
  }

  public static ColumnOp sqrt(UnnamedColumn column1) {
    return new ColumnOp("sqrt", Arrays.asList(column1));
  }

  public static ColumnOp avg(UnnamedColumn column1) {
    return new ColumnOp("avg", Arrays.asList(column1));
  }
  
  public static ColumnOp sum(UnnamedColumn column1) {
    return new ColumnOp("sum", Arrays.asList(column1));
  }

  public static ColumnOp pow(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("pow", Arrays.asList(column1, column2));
  }

  public static ColumnOp interval(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("interval", Arrays.asList(column1, column2));
  }

  public static ColumnOp date(UnnamedColumn column) {
    return new ColumnOp("date", column);
  }

  public static ColumnOp greater(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("greater", Arrays.asList(column1, column2));
  }

  public static ColumnOp less(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("less", Arrays.asList(column1, column2));
  }

  public static ColumnOp greaterequal(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("greaterequal", Arrays.asList(column1, column2));
  }

  public static ColumnOp lessequal(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("lessequal", Arrays.asList(column1, column2));
  }

  public static ColumnOp min(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("min", Arrays.asList(column1, column2));
  }

  public static ColumnOp max(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("max", Arrays.asList(column1, column2));
  }
  
  public static ColumnOp percentile(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("percentile", Arrays.asList(column1, column2));
  }

  public static ColumnOp isnull(UnnamedColumn column1) {
    return new ColumnOp("isnull", Arrays.asList(column1));
  }
  
  public static ColumnOp isnotnull(UnnamedColumn column1) {
    return new ColumnOp("isnotnull", Arrays.asList(column1));
  }

  public static ColumnOp like(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("like", Arrays.asList(column1, column2));
  }

  public static ColumnOp notlike(UnnamedColumn column1, UnnamedColumn column2) {
    return new ColumnOp("notlike", Arrays.asList(column1, column2));
  }

  public static ColumnOp exists(UnnamedColumn column) {
    return new ColumnOp("exists", column);
  }

  public static ColumnOp notexists(UnnamedColumn column) {
    return new ColumnOp("notexists", column);
  }

  public static ColumnOp between(UnnamedColumn column1, UnnamedColumn column2, UnnamedColumn column3) {
    return new ColumnOp("between", Arrays.asList(column1, column2, column3));
  }

  public static ColumnOp whenthenelse(List<UnnamedColumn> cols) {
    return new ColumnOp("whenthenelse", cols);
  }

  public static ColumnOp in(List<UnnamedColumn> columns) {
    return new ColumnOp("in", columns);
  }

  public static ColumnOp notin(List<UnnamedColumn> columns) {
    return new ColumnOp("notin", columns);
  }

  public static ColumnOp countdistinct(UnnamedColumn column) {
    return new ColumnOp("countdistinct", column);
  }

  public static ColumnOp year(UnnamedColumn column) {
    return new ColumnOp("year", column);
  }


  public static ColumnOp substr(UnnamedColumn column, UnnamedColumn from, UnnamedColumn to) {
    return new ColumnOp("substr", Arrays.asList(column, from, to));
  }

  public static ColumnOp rand() {
    return new ColumnOp("rand");
  }

  public static ColumnOp floor(UnnamedColumn column) {
    return new ColumnOp("floor", column);
  }
  
  public static ColumnOp cast(UnnamedColumn column, UnnamedColumn dataType) {
    return new ColumnOp("cast", Arrays.asList(column, dataType));
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

  public boolean isColumnOpAggregate() {
    if (this.getOpType().equals("avg") ||
        this.getOpType().equals("sum") ||
        this.getOpType().equals("count") ||
        this.getOpType().equals("max") ||
        this.getOpType().equals("min")) {
      return true;
    }
    boolean aggExists = false;
    List<UnnamedColumn> ops = this.getOperands();
    for (UnnamedColumn c : ops) {
      if (c instanceof ColumnOp) {
        if (((ColumnOp) c).isColumnOpAggregate()) {
          aggExists = true;
          break;
        }
      }
    }
    return aggExists;
  }

  @Override
  public boolean isAggregateColumn() {
    return isColumnOpAggregate();
  }

  @Override
  public ColumnOp deepcopy() {
    List<UnnamedColumn> newOperands = new ArrayList<>();
    for (UnnamedColumn operand:operands) {
      newOperands.add(operand.deepcopy());
    }
    return new ColumnOp(opType, newOperands);
  }

}