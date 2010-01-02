package com.maroontress.uncover;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
   ��ӥ����˴ؤ������򥫥ץ��벽���ޤ���
*/
public final class Revision {
    /** �ؿ��Υꥹ�ȤǤ��� */
    private List<Function> list;

    /** �����ȴؿ��ΥޥåפǤ��� */
    private Map<String, Function> keyMap;

    /** ʣ���٥�󥭥󥰤ΥޥåפǤ��� */
    private Map<Function, Integer> complexityRankMap;

    /**
       ���󥹥��󥹤��������ޤ���

       @param list �ؿ��Υꥹ��
    */
    public Revision(final List<Function> list) {
        this.list = list;
        keyMap = new HashMap<String, Function>();
        complexityRankMap = new HashMap<Function, Integer>();

	for (Function function : list) {
            keyMap.put(function.getKey(), function);
	}

        Function[] array = createFunctionArray();
        Arrays.sort(array, Function.COMPLEXITY_COMPARATOR);
	RankMapFactory complexityRankMapFactory = new RankMapFactory() {
	    public int getIntValue(final Function function) {
                return function.getComplexity();
	    }
	};
	complexityRankMap = complexityRankMapFactory.create(array);
    }

    /**
       �ؿ��ο���������ޤ���

       @return �ؿ��ο�
    */
    public int getSize() {
	return list.size();
    }

    /**
       ���Υ�ӥ����δؿ��Τ���������оݤΥ�ӥ����˴ޤޤ�ʤ���
       �Τ�ꥹ�ȤǼ������ޤ���

       @param target ����оݤΥ�ӥ����
       @return ����оݤΥ�ӥ����˴ޤޤ�ʤ��ؿ��Υꥹ��
    */
    public List<Function> createOuterFunctions(final Revision target) {
        List<Function> deltaList = new ArrayList<Function>();
        for (Function function : list) {
	    if (target.keyMap.get(function.getKey()) == null) {
                deltaList.add(function);
            }
        }
        return deltaList;
    }

    /**
       ���Υ�ӥ����δؿ��Τ���������оݤΥ�ӥ����ˤ�ޤޤ���
       �Τ�ؿ��ڥ��Υꥹ�ȤǼ������ޤ���

       �ꥹ�Ȥ����Ǥˤʤ�ؿ��ڥ��ϡ����Υ�ӥ����˽�°����ؿ�������
       ����оݤΥ�ӥ����˽�°����ؿ������ˤʤ�ޤ���

       @param target ����оݤΥ�ӥ����
       @return ����оݤΥ�ӥ����ˤ�ޤޤ��ؿ��δؿ��ڥ��Υꥹ��
    */
    public List<FunctionPair> createInnerFunctionPairs(final Revision target) {
        List<FunctionPair> pairList = new ArrayList<FunctionPair>();
        for (Function function : list) {
            Function targetFunction = target.keyMap.get(function.getKey());
            if (targetFunction == null) {
                continue;
            }
	    pairList.add(new FunctionPair(function, targetFunction));
        }
	return pairList;
    }

    /**
       ���Υ�ӥ����δؿ�������Ǽ������ޤ���

       @return �ؿ�������
    */
    public Function[] createFunctionArray() {
	return list.toArray(new Function[list.size()]);
    }

    /**
       ���Υ�ӥ����ˤ�����ؿ���ʣ���٥�󥯤�������ޤ���

       @param key �ؿ��Υ���
       @return ʣ���٥�󥯡��������б�����ؿ���¸�ߤ��ʤ�����0
    */
    public int getComplexityRank(final String key) {
	Function function = keyMap.get(key);
	if (function == null) {
	    return 0;
	}
	return complexityRankMap.get(function);
    }

    /**
       ���Υ�ӥ����δؿ���������ޤ���

       @param key �ؿ��Υ���
       @return �ؿ����������б�����ؿ���¸�ߤ��ʤ�����null
    */
    public Function getFunction(final String key) {
	return keyMap.get(key);
    }
}