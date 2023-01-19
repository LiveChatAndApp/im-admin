/*
 * This file is part of the Wildfire Chat package.
 * (c) Heavyrain2012 <heavyrain.lee@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package cn.wildfirechat.common.model.dto;

import lombok.Data;

@Data
public class HazelCastOperation {
    private static final Integer ADD_ACTION = 1;
    private static final Integer DEL_ACTION = 2;
    private static final Integer UPDATE_ACTION = 3;
    private static final Integer GET_ACTION = 4;

    private Integer action;
    private String mapName;
    private String key;
    private String value;

    public HazelCastOperation() {
    }

    public HazelCastOperation(Integer action, String mapName, String key, String value) {
        this.action = action;
        this.mapName = mapName;
        this.key = key;
        this.value = value;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
