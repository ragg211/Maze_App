package com.example.algorithm

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.LinkedList
import java.util.Queue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MazeView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val rowCount = 20
    private val columnCount = 15
    private var cellSize = 0f

    private val mazeMatrix = Array(rowCount) { IntArray(columnCount) }

    private val startRow = 0
    private val startColumn = 0
    private val endRow = rowCount - 1
    private val endColumn = columnCount - 1

    private val gridPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val wallPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val startPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    private val targetPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val explorationPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
    }

    private val pathPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private data class Point(val row: Int, val col: Int, val previous: Point?)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cellSize = (w / columnCount).toFloat()

        mazeMatrix[startRow][startColumn] = 2
        mazeMatrix[endRow][endColumn] = 3
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until rowCount) {
            for (j in 0 until columnCount) {
                val left = j * cellSize
                val top = i * cellSize
                val right = left + cellSize
                val bottom = top + cellSize

                when (mazeMatrix[i][j]) {
                    1 -> canvas.drawRect(left, top, right, bottom, wallPaint)
                    2 -> canvas.drawRect(left, top, right, bottom, startPaint)
                    3 -> canvas.drawRect(left, top, right, bottom, targetPaint)
                    4 -> canvas.drawRect(left, top, right, bottom, explorationPaint)
                    5 -> canvas.drawRect(left, top, right, bottom, pathPaint)
                }
            }
        }

        for (i in 0..rowCount) {
            canvas.drawLine(0f, i * cellSize, width.toFloat(), i * cellSize, gridPaint)
        }

        for (i in 0..columnCount) {
            canvas.drawLine(i * cellSize, 0f, i * cellSize, height.toFloat(), gridPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            val col = (event.x / cellSize).toInt()
            val row = (event.y / cellSize).toInt()

            if (row in 0 until rowCount && col in 0 until columnCount) {
                if (mazeMatrix[row][col] == 0) {
                    mazeMatrix[row][col] = 1
                    invalidate()
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    suspend fun solveWithBfs() {
        val queue: Queue<Point> = LinkedList()
        val visited = Array(rowCount) { BooleanArray(columnCount) }
        val directions = arrayOf(intArrayOf(-1, 0), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(0, 1))

        queue.add(Point(startRow, startColumn, null))
        visited[startRow][startColumn] = true

        var targetPoint: Point? = null

        while (queue.isNotEmpty()) {
            val current = queue.poll()!!

            if (current.row == endRow && current.col == endColumn) {
                targetPoint = current
                break
            }

            for (dir in directions) {
                val nextRow = current.row + dir[0]
                val nextCol = current.col + dir[1]

                if (nextRow in 0 until rowCount && nextCol in 0 until columnCount) {
                    if (!visited[nextRow][nextCol] && mazeMatrix[nextRow][nextCol] != 1) {
                        visited[nextRow][nextCol] = true
                        queue.add(Point(nextRow, nextCol, current))

                        if (mazeMatrix[nextRow][nextCol] == 0) {
                            mazeMatrix[nextRow][nextCol] = 4
                            withContext(Dispatchers.Main) {
                                invalidate()
                            }
                            delay(15)
                        }
                    }
                }
            }
        }

        var pathNode = targetPoint?.previous
        while (pathNode != null && pathNode.previous != null) {
            mazeMatrix[pathNode.row][pathNode.col] = 5
            withContext(Dispatchers.Main) {
                invalidate()
            }
            delay(30)
            pathNode = pathNode.previous
        }
    }
}