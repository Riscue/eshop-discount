package xyz.riscue.eshop.model;

import org.apache.log4j.Logger;
import xyz.riscue.eshop.utils.StringUtil;

@SuppressWarnings("unused")
public enum Region {
    ARGENTINA,
    AUSTRALIA,
    AUSTRIA,
    BELGIUM,
    BRAZIL,
    BULGARIA,
    CANADA,
    CHILE,
    COLOMBIA,
    CROATIA,
    CYPRUS,
    CZECH_REPUBLIC,
    DENMARK,
    ESTONIA,
    FINLAND,
    FRANCE,
    GERMANY,
    GREECE,
    HONG_KONG,
    HUNGARY,
    IRELAND,
    ISRAEL,
    ITALY,
    LATVIA,
    LITHUANIA,
    LUXEMBOURG,
    MALTA,
    MEXICO,
    NETHERLANDS,
    NEW_ZEALAND,
    NORWAY,
    PERU,
    POLAND,
    PORTUGAL,
    ROMANIA,
    RUSSIA,
    SLOVAKIA,
    SLOVENIA,
    SOUTH_AFRICA,
    SOUTH_KOREA,
    SPAIN,
    SWEDEN,
    SWITZERLAND,
    UNITED_KINGDOM,
    UNITED_STATES;

    public static Region find(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            Logger.getLogger(Region.class).debug(e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return StringUtil.capitalize(name().replace("_", " "));
    }
}
