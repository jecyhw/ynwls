package com.cn.bean;

import com.cn.util.JsonTimestampDeserialize;
import com.cn.util.JsonTimestampSerializer;
import com.cn.util.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jecyhw on 2014/10/27.
 */
public class ConditionEntity {
    String recorder;

    @JsonDeserialize(using = JsonTimestampDeserialize.class)
    @JsonSerialize(using = JsonTimestampSerializer.class)
    Timestamp startTime;

    @JsonDeserialize(using = JsonTimestampDeserialize.class)
    @JsonSerialize(using = JsonTimestampSerializer.class)
    Timestamp endTime;
    String address;
    Double left;//经度的左边界
    Double top;//纬度的左边界
    Double right;//经度的右边界
    Double bottom;//纬度的右边界

    String name;
    String keysiteslist;
    String annotation;

    String sql = null;
    List sqlValues = null;


    StringBuilder sb = new StringBuilder();

    public String getSql() {
        /*
        select b.* from (select t_tracks.* from t_tracks, (SELECT DISTINCT t_tracks_points.trackid FROM t_tracks_points WHERE
(( longitude>='-100.198368' and longitude<'181.461679' )
and( latitude>='-1.209283' and latitude<'180.959893' )))a where t_tracks.trackid = a.trackid)b;
        * */
        if (sql == null) {
            get();
        }
        return sql;
    }

    public List getSqlValues() {
        if (sqlValues == null) {
            get();
        }
        return sqlValues;
    }

    protected void get() {
        if (sqlValues == null) {
            sqlValues = new ArrayList();
        }
        List<String> and = new ArrayList<>();
        if (top != null || left != null || right != null || bottom != null) {
            //sb.append("select b.* from (select t_tracks.* from t_tracks, (select distinct t_tracks_points.trackid from t_tracks_points where (");
            sb.append("select b.* from (select ")
                    .append(TableName.getTracks())
                    .append(".* from ")
                    .append(TableName.getTracks())
                    .append(", (select distinct ")
                    .append(TableName.getTrackPoint())
                    .append(".trackid from ")
                    .append(TableName.getTrackPoint())
                    .append(" where ( ");

            if (left != null) {
                and.add("longitude >= ?");
                sqlValues.add(left);
            }

            if (right != null) {
                and.add("longitude <= ?");
                sqlValues.add(right);
            }

            if (top != null) {
                and.add("latitude >= ?");
                sqlValues.add(top);
            }

            if (bottom != null) {
                and.add("latitude <= ?");
                sqlValues.add(bottom);
            }
            sb.append(String.join(" and ", and));
            sb.append("))a where ")
                    .append(TableName.getTracks())
                    .append(".trackid = a.trackid)b");
        } else {
            //sb.append("select distinct * from t_tracks where ");
            sb.append("select * from ")
                    .append(TableName.getTracks());
        }

        and.clear();
        if (recorder != null) {
            and.add("author like ?");
            sqlValues.add("%" + recorder + "%");
        }
        if (startTime != null) {
            and.add("starttime >= ?");
            sqlValues.add(startTime.toString());
        }
        if (endTime != null) {
            and.add("endtime <= ?");
            sqlValues.add(endTime.toString());
        }
        if (address != null) {
            and.add("(name like ? or keysiteslist like ? or annotation like ?)");
            sqlValues.add("%" + address + "%");
            sqlValues.add("%" + address + "%");
            sqlValues.add("%" + address + "%");
        } else {
            if (name != null) {
                and.add("name like ?");
                sqlValues.add("%" + name + "%");
            }
            if (keysiteslist != null) {
                and.add("keysiteslist like ?");
                sqlValues.add("%" + keysiteslist + "%");
            }

            if (annotation != null) {
                and.add("annotation like ?");
                sqlValues.add("%" + annotation + "%");
            }
        }
        if (!and.isEmpty()) {
            sb.append(" where ").append(String.join(" and ", and));
        }
        sql = sb.toString();
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public Double getTop() {
        return top;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    public Double getRight() {
        return right;
    }

    public void setRight(Double right) {
        this.right = right;
    }

    public Double getBottom() {
        return bottom;
    }

    public void setBottom(Double bottom) {
        this.bottom = bottom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getKeysiteslist() {
        return keysiteslist;
    }

    public void setKeysiteslist(String keysiteslist) {
        this.keysiteslist = keysiteslist;
    }

    public boolean isEmpty() {
        if (recorder != null) {
            return false;
        }
        if (startTime != null) {
            return false;
        }
        if (endTime != null) {
            return false;
        }
        if (address != null) {
            return false;
        }
        if (left != null) {
            return false;
        }
        if (top != null) {
            return false;
        }
        if (right != null) {
            return false;
        }
        if (bottom != null) {
            return false;
        }
        if (name != null) {
            return false;
        }
        if (annotation != null) {
            return false;
        }
        if (keysiteslist != null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConditionEntity{" +
                "recorder='" + recorder + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", address='" + address + '\'' +
                ", left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
    }
}
