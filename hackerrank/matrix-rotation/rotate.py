def rotate(matrix, rows, cols, rotations):
    # convert the matrix into list of layers, sorted from outermost to innermost
    disassembles = __disassemble(matrix, rows, cols)

    # apply rotations on layer list
    rotated = __rotate_layers(disassembles, rotations)

    # convert list of layers back into the original matrix
    return __assemble(rotated, rows, cols)

def __disassemble(matrix, rows, cols):
    disassembles = []
    size = int(min(rows, cols) / 2)

    for i in range(size):
        layer = []
        # top
        for j in range(i, cols-i):
            layer.append(matrix[i][j])
        # right
        for j in range(i+1, rows-i-1):
            layer.append(matrix[j][cols-i-1])
        # bottom
        for j in reversed(range(i, cols-i)):
            layer.append(matrix[rows-i-1][j])
        # left
        for j in reversed(range(i+1, rows-i-1)):
            layer.append(matrix[j][i])
        disassembles.append(layer)

    return disassembles

def __rotate_layers(disassembles, rotations):
    assembles = []
    for layer in disassembles:
        list_rotations = rotations % len(layer)
        assemble = []
        for i in range(len(layer)):
            assemble.append(layer[(i+list_rotations) % len(layer)])
        assembles.append(assemble)
    return assembles

def __assemble(assembles, rows, cols):
    matrix = []
    size = int(min(rows, cols) / 2)

    for _ in range(rows):
        row = []
        for _ in range(cols):
            row.append(0)
        matrix.append(row)

    for i in range(size):
        layer = assembles[i]
        # top
        j = 0
        for k in range(i, cols-i):
            matrix[i][k] = layer[j]
            j += 1
        # right
        for k in range(i+1, rows-i-1):
            matrix[k][cols-i-1] = layer[j]
            j += 1
        # bottom
        for k in reversed(range(i, cols-i)):
            matrix[rows-i-1][k] = layer[j]
            j += 1
        # left
        for k in reversed(range(i+1, rows-i-1)):
            matrix[k][i] = layer[j]
            j += 1
    return matrix
