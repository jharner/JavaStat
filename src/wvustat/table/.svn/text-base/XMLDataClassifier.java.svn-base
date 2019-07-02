package wvustat.table;

import java.io.*;
import java.net.*;
import java.util.*;


public class XMLDataClassifier {
	
	private HashMap subjectMap = new HashMap(); //<String, <HashSet>>
	private HashMap descriptionMap = new HashMap(); //<String, String>
	
	
	public XMLDataClassifier(String dataUrl, Vector modules) throws MalformedURLException, IOException, ParseException
	{
		
		// Fetch all xml files into dataTags
		Vector dataTags=new Vector();

		URL url=new URL(dataUrl);
		InputStreamReader isr=new InputStreamReader(url.openStream());
		BufferedReader bufReader=new BufferedReader(isr);
		
		StringBuffer buf=new StringBuffer();
		String line=null;
		while((line=bufReader.readLine())!=null){
			buf.append(line);
		}
		
		SimpleHTMLParser parser=new SimpleHTMLParser(buf.toString());
		Vector tags=parser.getTags();
		
		for(int i=0;i<tags.size();i++){
			AnchorTag aTag=(AnchorTag)tags.elementAt(i);
			if(aTag.getAnchorText().endsWith("xml")){
				dataTags.addElement(aTag.getAnchorText());
			}
		}
		
		// Parse each xml file 
		for(int i=0; i<dataTags.size(); i++){
			String dsname = (String)dataTags.elementAt(i);
			url = new URL(dataUrl + dsname.trim());
		
			isr = new InputStreamReader(url.openStream());  
			XMLDataParser xmlparser = new XMLDataParser();
            DataSetTM ds = xmlparser.parse(isr);
            
            // Get subject
            String subjects = ds.getDataSet().getSubject();
            String references = ds.getDataSet().getReference();
            
            // Check references (modules)
            if(references != null) {
            	List lst = Arrays.asList(references.replaceAll(" ", "").split(","));
            	ArrayList lst1 = new ArrayList(lst);
            	lst1.retainAll(modules);
            	if (lst1.size() == 0)
            		continue;
            } else 
            	continue;
            
            if(subjects != null) {
            	StringTokenizer st = new StringTokenizer(subjects, ",");
            	while (st.hasMoreTokens()) {
            		String subject = st.nextToken();
            		putSubjectInMap(subject.trim(), dsname);
            	}
            }
            
            // Get description
            String description = ds.getDataSet().getDescription();
            if(description != null)
            	descriptionMap.put(dsname, description);
		}
	}

	private void putSubjectInMap(String subject, String dsname) {
		if (subjectMap.containsKey(subject)) {
			HashSet set = (HashSet) subjectMap.get(subject);
			set.add(dsname);
		} else {
			HashSet set = new HashSet();
			set.add(dsname);
			subjectMap.put(subject, set);
		}
	}

	public HashMap getSubjectMap() {
		return subjectMap;
	}
	
	public HashMap getDescriptionMap() {
		return descriptionMap;
	}
	
	public static void main(String[] args) {
		try {
			XMLDataClassifier classifier = new XMLDataClassifier("http://ideal.stat.wvu.edu/ideal/datasets/", new Vector());
			HashMap map = classifier.getSubjectMap();
			
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String subject = (String)it.next();
				System.out.println("\nsubject:" + subject);
					
				Iterator dsit = ((Set)map.get(subject)).iterator();
				while (dsit.hasNext())
					System.out.println("data:" + dsit.next());
			}
			
			System.out.println();
			map = classifier.getDescriptionMap();
			it = map.keySet().iterator();
			while (it.hasNext()) {
				String dsname = (String)it.next();
				System.out.println(dsname + ":" + map.get(dsname));
			}
			
		} catch (Exception ex) {}

	}

}
