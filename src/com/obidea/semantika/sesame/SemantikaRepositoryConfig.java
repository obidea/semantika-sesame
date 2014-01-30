package com.obidea.semantika.sesame;

import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.util.GraphUtil;
import org.openrdf.model.util.GraphUtilException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.config.RepositoryImplConfigBase;

import com.obidea.semantika.util.StringUtils;

public class SemantikaRepositoryConfig extends RepositoryImplConfigBase
{
   public static final String NAMESPACE = "http://www.obidea.com/semantika#"; //$NON-NLS-1$

   private static ValueFactory sValueFactory = ValueFactoryImpl.getInstance();

   /** <tt>http://www.obidea.com/semantika#cfgpath</tt> */
   public final static URI SEMANTIKA_CONFIG_PATH = sValueFactory.createURI(NAMESPACE + "cfgpath"); //$NON-NLS-1$

   private String mConfigurationPath = ""; //$NON-NLS-1$

   public SemantikaRepositoryConfig()
   {
      super(SemantikaRepositoryFactory.REPOSITORY_TYPE);
   }

   public String getConfigurationPath()
   {
      return mConfigurationPath;
   }

   void setConfigurationPath(String path)
   {
      mConfigurationPath = path;
   }

   @Override
   public void validate() throws RepositoryConfigException
   {
       if (StringUtils.isEmpty(mConfigurationPath)) {
           throw new RepositoryConfigException("Configuration path is empty"); //$NON-NLS-1$
       }
   }

   @Override
   public Resource export(Graph graph)
   {
      Resource implNode = super.export(graph);

      if (!StringUtils.isEmpty(mConfigurationPath)) {
         graph.add(implNode, SEMANTIKA_CONFIG_PATH, sValueFactory.createLiteral(mConfigurationPath));
      }
      return implNode;
   }

   @Override
   public void parse(Graph graph, Resource implNode) throws RepositoryConfigException
   {
      super.parse(graph, implNode);
      try {
         Literal configPath = GraphUtil.getOptionalObjectLiteral(graph, implNode, SEMANTIKA_CONFIG_PATH);
         if (configPath != null) {
            setConfigurationPath(configPath.getLabel());
         }
      }
      catch (GraphUtilException e) {
         throw new RepositoryConfigException(e);
      }
   }
}
