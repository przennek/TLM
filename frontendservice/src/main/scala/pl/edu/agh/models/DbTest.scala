package pl.edu.agh.models

import java.util.UUID

case class DbTest(
                 tokenId: UUID,
                 className: String,
                 moduleName: String,
                 editLog: List[String],
                 jsonData: String
               )