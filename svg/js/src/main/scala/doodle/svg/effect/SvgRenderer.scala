package doodle
package svg
package effect

import cats.effect.IO
import doodle.core.Transform
import doodle.effect.Renderer
import doodle.svg.algebra.Algebra
import scalatags.JsDom

object SvgRenderer extends Renderer[Algebra, Drawing, Frame, Canvas] {
  val svg = Svg(JsDom)

  def frame(description: Frame): IO[Canvas] =
    IO{ Canvas.fromFrame(description) }

  def render[A](canvas: Canvas)(image: Image[A]): IO[A] =
    for {
      drawing <- IO { image(Algebra) }
      (bb, rdr) = drawing.runA(List.empty).value
      (_, fa) = rdr.run(Transform.identity).value
      (r, a) = fa.run.value
      nodes = svg.render(bb, r).render
      _ = canvas.setSvg(nodes)
    } yield a

}