/**
 * 
 */
package nframe;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;

/**
 * @author lvsheng.huang
 * 框架核心,逻辑类模块
 */

public class NFLogicClassModule extends NFILogicClassModule{
	private Map<String, NFILogicClass> logicClasses = new Hashtable<String, NFILogicClass>();
	
	private NFIElementInfoModule elementInfoModule;
	private String configFileName;
	
	@Override
	public void init(){
		configFileName = "./DataCfg/Struct/LogicClass.xml";
		
		load();
	}
	
	@Override
	public void afterInit(){
	}
	
	@Override
	public void beforeShut(){
		clear();
	}
	
	@Override
	public void shut(){
	}
	
	@Override
	public void execute(){
	}
	
	@Override
	public boolean load(){
		
		SAXReader saxReader = new SAXReader();

        Document document = null;
		try {
			document = saxReader.read(new File(configFileName));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Element root = document.getRootElement();
		List<Element> baseObjectList = root.elements();
        
        Iterator<Element> it = baseObjectList.iterator();
        while (it.hasNext()) {
        	
        	Element element = it.next();
        	load(element, null);
        
        }
        
        //m_pElementInfoModule->Load();
        
		return false;
	}
	
	@Override
	public boolean clear(){
		return false;
	}
	
	@Override
	public boolean reload(String strClassName){
		return false;
	}
	
	@Override
	public NFIPropertyManager getPropertyManager(String className){
/*		NF_SHARED_PTR<NFILogicClass> pClass = GetElement(strClassName);
		if (pClass.get())
		{
			return pClass->GetPropertyManager();
		}*/

		return null;
	}

	public boolean load(Element element, NFILogicClass parentClass){
		String name = element.attribute("Id").getName();
		String type = element.attribute("Type").getName();
		String path = element.attribute("Path").getName();
		String instancePath = element.attribute("InstancePath").getName();
		
		if(this.logicClasses.get(name) == null)
		{
			return false;
		}

		NFILogicClass logicClass = new NFLogicClass();
		this.logicClasses.put(name, logicClass);
		
		logicClass.setParentClass(parentClass);
		logicClass.setTypeName(type);
		logicClass.setInstancePath(instancePath);
		
		addClass(path, logicClass);
	
		List<Element> nodeIterator = element.elements();
		Iterator<Element> it = nodeIterator.iterator();
		while (it.hasNext()) {
        	
        	Element childElement = it.next();
        	
        	load(childElement, parentClass);
        
        }		
        
		return true;	
	}
	
	private NFIData.Type computerType(String typeName, NFIData var){
		NFIData.Type type = NFIData.Type.UNKNOW;
		if ("int".equals(typeName)){
			var.set(NFIData.INT_NIL);
			type = NFIData.Type.INT;
		}else if ("float".equals(typeName)){
			var.set(NFIData.FLOAT_NIL);
			type = NFIData.Type.FLOAT;
		}else if ("string".equals(typeName)){
			var.set(NFIData.STRING_NIL);
			type = NFIData.Type.STRING;
		}else if ("object".equals(typeName)){
			var.set(NFIData.OBJECT_NIL);
			type = NFIData.Type.OBJECT;
		}
		return type;
	}

    private boolean addPropertys(Element element, NFILogicClass logicClass)
    {
    	List<Element> nodeIterator = element.elements();
		Iterator<Element> it = nodeIterator.iterator();
		while (it.hasNext()) {
        	
        	Element childElement = it.next();
        	
        	String name = childElement.attribute("Id").getName();
        	if(null != logicClass.getPropertyManager().getProperty(name))
        	{
        		continue;
        	}
        	

        	String typeProperty = childElement.attribute("Type").getName();
        	String publicProperty = childElement.attribute("Public").getName();
        	String privateProperty = childElement.attribute("Private").getName();
        	String indexProperty = childElement.attribute("Index").getName();
        	String saveProperty = childElement.attribute("Save").getName();
        	String viewProperty = childElement.attribute("View").getName();
        	String relationValueProperty = childElement.attribute("RelationValue").getName();
        	
        	NFIData data = new NFData();
        	if (NFIData.Type.UNKNOW == computerType(typeProperty, data))
            {
        		//assert();
            }
        	
        	//int a=Integer.parseInt(s);
        	
        }	
		
    	return true;
    }
    
    private boolean addRecords(Element childElement, NFILogicClass logicClass)
    {
    	
    	
    	return true;
    }
    

	private boolean addClass(String classFilePath, NFILogicClass parentClass){
		
		NFILogicClass parent = parentClass.getParentClass();
		while(null != parent){
			
			List<String> fileList = parent.getFileList();
			Iterator<String> it = fileList.iterator();
			while (it.hasNext()) {
	        	
	        	String file = it.next();
	        	
	        	if (addClassInclude(file, parentClass))
	            {
	        		parentClass.addFile(file);
	            }
			}
			
			parent = parent.getParentClass();
		}
		
		 //////////////////////////////////////////////////////////////////////////
		
	    if (addClassInclude(classFilePath, parentClass))
	    {
	    	parentClass.addFile(classFilePath);
	    }
		
		
		return true;
	}
	
	private boolean addClassInclude(String classFilePath, NFILogicClass logicClass)
	{
		return false;
	}
	
}
