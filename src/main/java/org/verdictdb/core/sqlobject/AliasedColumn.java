package org.verdictdb.core.sqlobject;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents "column as aliasName".
 *
 * @author Yongjoo Park
 */
public class AliasedColumn implements SelectItem, Serializable {

  private static final long serialVersionUID = 8840737358290041703L;

  UnnamedColumn column;

  String aliasName;

  public AliasedColumn(UnnamedColumn column, String aliasName) {
    this.column = column;
    this.aliasName = aliasName;
  }

  public UnnamedColumn getColumn() {
    return column;
  }

  public void setColumn(UnnamedColumn column) {
    this.column = column;
  }

  public String getAliasName() {
    return aliasName;
  }

  public void setAliasName(String aliasName) {
    this.aliasName = aliasName;
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

  @Override
  public boolean isAggregateColumn() {
    return column.isAggregateColumn();
  }

  @Override
  public SelectItem deepcopy() {
    return new AliasedColumn(column.deepcopy(), this.aliasName);
  }

}
