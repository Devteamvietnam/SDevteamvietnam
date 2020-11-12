package com.devteamvietnam.common.core.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Set;

import com.devteamvietnam.common.utils.StringUtils;


/**
 * Type converter
 *
 * @author Ivan
 */
public class Convert
{
    /**
     * Convert to string<br>
     * If the given value is null, or the conversion fails, return to the default value<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static String toStr(Object value, String defaultValue)
    {
        if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof String)
        {
            return (String) value;
        }
        return value.toString();
    }

    /**
     * Convert to string<br>
     * If the given value is <code>null</code>, or the conversion fails, return the default value <code>null</code><br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static String toStr(Object value)
    {
        return toStr(value, null);
    }

    /**
     * Convert to character<br>
     * If the given value is null, or the conversion fails, return to the default value<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Character toChar(Object value, Character defaultValue)
    {
        if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof Character)
        {
            return (Character) value;
        }

        final String valueStr = toStr(value, null);
        return StringUtils.isEmpty(valueStr)? defaultValue: valueStr.charAt(0);
    }

    /**
     * Convert to character<br>
     * If the given value is <code>null</code>, or the conversion fails, return the default value <code>null</code><br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Character toChar(Object value)
    {
        return toChar(value, null);
    }

    /**
     * Convert to byte<br>
     * If the given value is <code>null</code>, or the conversion fails, return to the default value<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Byte toByte(Object value, Byte defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Byte)
        {
            return (Byte) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).byteValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Byte.parseByte(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to byte<br>
     * If the given value is <code>null</code>, or the conversion fails, return the default value <code>null</code><br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Byte toByte(Object value)
    {
        return toByte(value, null);
    }

    /**
     * Convert to Short<br>
     * If the given value is <code>null</code>, or the conversion fails, return to the default value<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Short toShort(Object value, Short defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Short)
        {
            return (Short) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).shortValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Short.parseShort(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to Short<br>
     * If the given value is <code>null</code>, or the conversion fails, return the default value <code>null</code><br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Short toShort(Object value)
    {
        return toShort(value, null);
    }
    
    /**
     * Convert to Number<br>
     * If the given value is empty or the conversion fails, the default value will be returned.<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Number toNumber(Object value, Number defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Number)
        {
            return (Number) value;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return NumberFormat.getInstance().parse(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to Number<br>
     * If the given value is empty or the conversion fails, the default value <code>null</code><br> is returned
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Number toNumber(Object value)
    {
        return toNumber(value, null);
    }

    /**
     * Convert to int<br>
     * If the given value is empty or the conversion fails, the default value will be returned.<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Integer toInt(Object value, Integer defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Integer)
        {
            return (Integer) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Integer.parseInt(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to int<br>
     * If the given value is <code>null</code>, or the conversion fails, return the default value <code>null</code><br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Integer toInt(Object value)
    {
        return toInt(value, null);
    }

    /**
     * Converted to Integer array<br>
     *
     * @param str converted value
     * @return result
     */
    public static Integer[] toIntArray(String str)
    {
        return toIntArray(",", str);
    }

    /**
     * Convert to Long array<br>
     *
     * @param str converted value
     * @return result
     */
    public static Long[] toLongArray(String str)
    {
        return toLongArray(",", str);
    }
    
    /**
     * Converted to Integer array<br>
     *
     * @param split separator
     * @param split converted value
     * @return result
     */
    public static Integer[] toIntArray(String split, String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new Integer[] {};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i <arr.length; i++)
        {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     * Convert to Long array<br>
     *
     * @param split separator
     * @param str converted value
     * @return result
     */
    public static Long[] toLongArray(String split, String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new Long[] {};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i <arr.length; i++)
        {
            final Long v = toLong(arr[i], null);
            longs[i] = v;
        }
        return longs;
    }

    /**
     * Convert to String array<br>
     *
     * @param str converted value
     * @return result
     */
    public static String[] toStrArray(String str)
    {
        return toStrArray(",", str);
    }

    /**
     * Convert to String array<br>
     *
     * @param split separator
     * @param split converted value
     * @return result
     */
    public static String[] toStrArray(String split, String str)
    {
        return str.split(split);
    }

    /**
     * Convert to long<br>
     * If the given value is empty or the conversion fails, the default value will be returned.<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Long toLong(Object value, Long defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Long)
        {
            return (Long) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            // Support scientific notation
            return new BigDecimal(valueStr.trim()).longValue();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to long<br>
     * If the given value is <code>null</code>, or the conversion fails, return the default value <code>null</code><br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Long toLong(Object value)
    {
        return toLong(value, null);
    }

    /**
     * Convert to double<br>
     * If the given value is empty or the conversion fails, the default value will be returned.<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Double toDouble(Object value, Double defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Double)
        {
            return (Double) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).doubleValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            // Support scientific notation
            return new BigDecimal(valueStr.trim()).doubleValue();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to double<br>
     * If the given value is empty or the conversion fails, the default value <code>null</code><br> is returned
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Double toDouble(Object value)
    {
        return toDouble(value, null);
    }
    /**
     * Convert to Float<br>
     * If the given value is empty or the conversion fails, the default value will be returned.<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Float toFloat(Object value, Float defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Float)
        {
            return (Float) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).floatValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Float.parseFloat(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to Float<br>
     * If the given value is empty or the conversion fails, the default value <code>null</code><br> is returned
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Float toFloat(Object value)
    {
        return toFloat(value, null);
    }

    /**
     * Convert to boolean<br>
     * String supported values: true, false, yes, ok, no, 1,0. If the given value is empty or the conversion fails, the default value will be returned.<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static Boolean toBool(Object value, Boolean defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Boolean)
        {
            return (Boolean) value;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        valueStr = valueStr.trim().toLowerCase();
        switch (valueStr)
        {
            case "true":
                return true;
            case "false":
                return false;
            case "yes":
                return true;
            case "ok":
                return true;
            case "no":
                return false;
            case "1":
                return true;
            case "0":
                return false;
            default:
                return defaultValue;
        }
    }

    /**
     * Convert to boolean<br>
     * If the given value is empty or the conversion fails, the default value <code>null</code><br> is returned
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static Boolean toBool(Object value)
    {
        return toBool(value, null);
    }

    /**
     * Convert to Enum object<br>
     * If the given value is empty or the conversion fails, the default value will be returned.<br>
     *
     * @param clazz Enum Class
     * @param value
     * @param defaultValue default value
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (clazz.isAssignableFrom(value.getClass()))
        {
            @SuppressWarnings("unchecked")
            E myE = (E) value;
            return myE;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Enum.valueOf(clazz, valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to Enum object<br>
     * If the given value is empty or the conversion fails, the default value <code>null</code><br> is returned
     *
     * @param clazz Enum Class
     * @param value
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value)
    {
        return toEnum(clazz, value, null);
    }

    /**
     * Convert to BigInteger<br>
     * If the given value is empty or the conversion fails, the default value will be returned.<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static BigInteger toBigInteger(Object value, BigInteger defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof BigInteger)
        {
            return (BigInteger) value;
        }
        if (value instanceof Long)
        {
            return BigInteger.valueOf((Long) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigInteger(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }
    /**
     * Convert to BigInteger<br>
     * If the given value is empty or the conversion fails, the default value <code>null</code><br> is returned
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static BigInteger toBigInteger(Object value)
    {
        return toBigInteger(value, null);
    }

    /**
     * Convert to BigDecimal<br>
     * If the given value is empty or the conversion fails, the default value will be returned<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @param defaultValue The default value when the conversion error occurs
     * @return result
     */
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof BigDecimal)
        {
            return (BigDecimal) value;
        }
        if (value instanceof Long)
        {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Double)
        {
            return new BigDecimal((Double) value);
        }
        if (value instanceof Integer)
        {
            return new BigDecimal((Integer) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigDecimal(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Convert to BigDecimal<br>
     * If the given value is empty or the conversion fails, the default value will be returned<br>
     * No error will be reported if the conversion fails
     *
     * @param value The converted value
     * @return result
     */
    public static BigDecimal toBigDecimal(Object value)
    {
        return toBigDecimal(value, null);
    }

    /**
     * Convert the object to a string<br>
     * 1. Byte array and ByteBuffer will be converted into corresponding string array 2. Object array will call Arrays.toString method
     *
     * @param obj object
     * @return string
     */
    public static String utf8Str(Object obj)
    {
        return str(obj, CharsetKit.CHARSET_UTF_8);
    }

    /**
     * Convert the object to a string<br>
     * 1. Byte array and ByteBuffer will be converted to corresponding string array 2. Object array will call Arrays.toString method
     *
     * @param obj object
     * @param charsetName character set
     * @return string
     */
    public static String str(Object obj, String charsetName)
    {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     * Convert the object to a string<br>
     * 1. Byte array and ByteBuffer will be converted to corresponding string array 2. Object array will call Arrays.toString method
     *
     * @param obj object
     * @param charset character set
     * @return string
     */
    public static String str(Object obj, Charset charset)
    {
        if (null == obj)
        {
            return null;
        }

        if (obj instanceof String)
        {
            return (String) obj;
        }
        else if (obj instanceof byte[] || obj instanceof Byte[])
        {
            return str((Byte[]) obj, charset);
        }
        else if (obj instanceof ByteBuffer)
        {
            return str((ByteBuffer) obj, charset);
        }
        return obj.toString();
    }

    /**
     * Convert byte array to string
     *
     * @param bytes byte array
     * @param charset character set
     * @return string
     */
    public static String str(byte[] bytes, String charset)
    {
        return str(bytes, StringUtils.isEmpty(charset)? Charset.defaultCharset(): Charset.forName(charset));
    }
    
    /**
     * Decode bytecode
     *
     * @param data string
     * @param charset character set, if this field is empty, the result of decoding depends on the platform
     * @return decoded string
     */
    public static String str(byte[] data, Charset charset)
    {
        if (data == null)
        {
            return null;
        }

        if (null == charset)
        {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * Convert the encoded byteBuffer data to a string
     *
     * @param data data
     * @param charset character set, if it is empty, use the current system character set
     * @return string
     */
    public static String str(ByteBuffer data, String charset)
    {
        if (data == null)
        {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * Convert the encoded byteBuffer data to a string
     *
     * @param data data
     * @param charset character set, if it is empty, use the current system character set
     * @return string
     */
    public static String str(ByteBuffer data, Charset charset)
    {
        if (null == charset)
        {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    // ------------------------------------------------ ----------------------- Full-width half-width conversion
    /**
     * Half-width to full-width
     *
     * @param input String.
     * @return Full-width string.
     */
    public static String toSBC(String input)
    {
        return toSBC(input, null);
    }

    /**
     * Half-width to full-width
     *
     * @param input String
     * @param notConvertSet does not replace the character set
     * @return Full-width string.
     */
    public static String toSBC(String input, Set<Character> notConvertSet)
    {
        char c[] = input.toCharArray();
        for (int i = 0; i <c.length; i++)
        {
            if (null != notConvertSet && notConvertSet.contains(c[i]))
            {
                // Skip characters that are not replaced
                continue;
            }

            if (c[i] == ' ')
            {
                c[i] ='\u3000';
            }
            else if (c[i] <'\177')
            {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * Full angle to half angle
     *
     * @param input String.
     * @return half-width string
     */
    public static String toDBC(String input)
    {
        return toDBC(input, null);
    }

    /**
     * Replace full-width with half-width
     *
     * @param text text
     * @param notConvertSet does not replace the character set
     * @return replaced characters
     */
    public static String toDBC(String text, Set<Character> notConvertSet)
    {
        char c[] = text.toCharArray();
        for (int i = 0; i <c.length; i++)
        {
            if (null != notConvertSet && notConvertSet.contains(c[i]))
            {
                // Skip characters that are not replaced
                continue;
            }

            if (c[i] =='\u3000')
            {
                c[i] = ' ';
            }
            else if (c[i]>'\uFF00' && c[i] <'\uFF5F')
            {
                c[i] = (char) (c[i]-65248);
            }
        }
        String returnString = new String(c);

        return returnString;
    }

    /**
     * Number and amount conversion to uppercase, first write a complete one and then replace such as zero with zero
     *
     * @param n number
     * @return Chinese uppercase numbers
     */
    public static String digitUppercase(double n)
    {
        String[] fraction = {"angle", "Minute"};
        String[] digit = {"zero", "one", "two", "three", "4", "5", "6", "7", "8", "9", "10" };
        String[][] unit = {{"vnd", "usd", "10,000", "billion" }, {"", "take", "hundred", "1,000"} };

        String head = n <0? "Negative": "";
        n = Math.abs(n);

        String s = "";
        for (int i = 0; i <fraction.length; i++)
        {
            s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i))% 10)] + fraction[i]).replaceAll("(zero.)+", "" );
        }
        if (s.length() <1)
        {
            s = "whole";
        }
        int integerPart = (int) Math.floor(n);

        for (int i = 0; i <unit[0].length && integerPart> 0; i++)
        {
            String p = "";
            for (int j = 0; j <unit[1].length && n> 0; j++)
            {
                p = digit[integerPart% 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(zero.)*zero$", "").replaceAll("^$", "zero") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*zero vnd", "usd").replaceFirst("(zero.)+", "").replaceAll("(zero.)+", "zero") .replaceAll("^whole$", "zero usd integer");
    }
 }
