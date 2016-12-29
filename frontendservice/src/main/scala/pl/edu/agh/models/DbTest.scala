package pl.edu.agh.models

/**
  * Created by Kamil on 29.12.2016.
  */
import java.util.UUID

case class DbTest(
                 tokenId: UUID,
                 className: String,
                 moduleName: String,
                 editLog: List[String],
                 jsonData: String
               )