# def solution(xs):
#     negatives = [num for num in xs if num < 0]
#     positives = [num for num in xs if num > 0]
#
#     if len(negatives) % 2 != 0:
#         negatives.sort()
#         negatives.pop()
#
#     if positives or negatives:
#         product = 1
#         for x in positives + negatives:
#             product *= x
#
#         return str(product)
#
#     return str(xs[0])